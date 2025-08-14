# module "srch_snet" {
#   source               = "github.com/pagopa/terraform-azurerm-v4//subnet?ref=v7.20.0"
#   name                 = "${var.project}-srch-snet-01"
#   resource_group_name  = var.virtual_network.resource_group_name
#   virtual_network_name = var.virtual_network.name
#   address_prefixes     = local.snet_cidrs
# }
#
# resource "azurerm_private_endpoint" "srch" {
#   depends_on          = [azurerm_search_service.srch]
#   name                = "${var.project}-${var.application_basename}-srch-pep-01"
#   resource_group_name = var.resource_group_name
#   location            = var.location
#   subnet_id           = var.peps_snet_id
#
#   private_service_connection {
#     name                           = azurerm_search_service.srch.name
#     private_connection_resource_id = azurerm_search_service.srch.id
#     is_manual_connection           = false
#     subresource_names              = ["searchService"]
#   }
#
#   private_dns_zone_group {
#     name                 = "${var.project}-${var.application_basename}-dns-zone-group-01"
#     private_dns_zone_ids = [data.azurerm_private_dns_zone.privatelink_srch.id]
#   }
#
#   tags = var.tags
# }

# # Must be approved manually in cosmosdb networking page
# resource "azurerm_search_shared_private_link_service" "srch_to_cosmos" {
#   name               = "${var.project}-search-service-spl-01"
#   search_service_id  = azurerm_search_service.search_engine_service.id
#   subresource_name   = "mongodb"
#   target_resource_id = data.azurerm_cosmosdb_account.cosmosdb.id
#   request_message    = "Enable access from AI Search to CMS services CosmosDB"
# }
data "azurerm_private_dns_zone" "cosmosdb" {
  name                = "privatelink.mongo.cosmos.azure.com"
  resource_group_name = "${var.prefix}-${var.env_short}-vnet-rg"
}

data "azurerm_subnet" "cosmosdb" {
  name                 = "${var.prefix}-${var.env_short}-cosmosb-mongodb-snet"
  virtual_network_name = "${var.prefix}-${var.env_short}-vnet"
  resource_group_name  = "${var.prefix}-${var.env_short}-vnet-rg"
}

resource "azurerm_private_endpoint" "search_pe" {
  name                = "${var.prefix}-${var.env_short}-search-service-pe"
  location            = var.location
  resource_group_name = azurerm_resource_group.search_engine_rg.name
  subnet_id           = data.azurerm_subnet.cosmosdb.id

  private_service_connection {
    name                           = "search-service-connection"
    is_manual_connection           = false
    private_connection_resource_id = azurerm_search_service.search_engine_service.id

    # Per AI Search, la sotto-risorsa è sempre "searchService"
    subresource_names              = ["searchService"]
  }

  private_dns_zone_group {
    name                 = "private-dns-zone-group"
    private_dns_zone_ids = [data.azurerm_private_dns_zone.cosmosdb.id]
  }
}