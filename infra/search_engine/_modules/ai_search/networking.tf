resource "azurerm_subnet" "srch_snet" {
  name                 = "${var.prefix}-${var.env_short}-srch-snet"
  resource_group_name  = data.azurerm_resource_group.rg_vnet.name
  virtual_network_name = data.azurerm_virtual_network.vnet.name
  address_prefixes     = var.cidr_subnet
}

resource "azurerm_private_dns_zone" "privatelink_srch_azure_com" {
  name                = "privatelink.search.windows.net"
  resource_group_name = data.azurerm_resource_group.rg_vnet.name

  tags = var.tags
}

resource "azurerm_private_dns_zone_virtual_network_link" "privatelink_srch_azure_com_vnet" {
  name                  = "${data.azurerm_virtual_network.vnet.name}-dns-link"
  resource_group_name   = data.azurerm_resource_group.rg_vnet.name
  private_dns_zone_name = azurerm_private_dns_zone.privatelink_srch_azure_com.name
  virtual_network_id    = data.azurerm_virtual_network.vnet.id
  registration_enabled  = false

  tags = var.tags
}


resource "azurerm_private_endpoint" "srch_pe" {
  name                = "${var.prefix}-${var.env_short}-srch-pep-01"
  location            = var.location
  resource_group_name = data.azurerm_resource_group.rg_vnet.name
  subnet_id           = azurerm_subnet.srch_snet.id

  private_service_connection {
    name                           = azurerm_search_service.search_engine_service.name
    is_manual_connection           = false
    private_connection_resource_id = azurerm_search_service.search_engine_service.id
    subresource_names              = ["searchService"]
  }

  private_dns_zone_group {
    name                 = "default"
    private_dns_zone_ids = [azurerm_private_dns_zone.privatelink_srch_azure_com.id]
  }
}

resource "azurerm_private_dns_a_record" "dns_a_record" {
  name                = azurerm_search_service.search_engine_service.name
  zone_name           = azurerm_private_dns_zone.privatelink_srch_azure_com.name
  resource_group_name = data.azurerm_resource_group.rg_vnet.name
  ttl                 = 10
  records             = [azurerm_private_endpoint.srch_pe.private_service_connection[0].private_ip_address]
}

# Network Security Group per la subnet degli endpoint
resource "azurerm_network_security_group" "nsg_private_endpoints" {
  name                = "${var.prefix}-${var.env_short}-srch-nsg-pep-01"
  location            = var.location
  resource_group_name = data.azurerm_resource_group.rg_vnet.name

  security_rule {
    name                       = "AllowHTTPS"
    priority                   = 1001
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "443"
    source_address_prefix      = "VirtualNetwork"
    destination_address_prefix = "*"
  }
}

# Associazione NSG alla subnet
resource "azurerm_subnet_network_security_group_association" "nsg_private_endpoints" {
  subnet_id                 = azurerm_subnet.srch_snet.id
  network_security_group_id = azurerm_network_security_group.nsg_private_endpoints.id
}


