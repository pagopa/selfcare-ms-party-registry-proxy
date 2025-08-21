resource "azurerm_resource_group" "srch_rg" {
  name     = "${var.project}-search-engine-rg"
  location = var.location

  tags = var.tags
}
# Azure AI Search Service
resource "azurerm_search_service" "srch_service" {
  name                = "${var.project}-search-service"
  resource_group_name = azurerm_resource_group.srch_rg.name
  location            = azurerm_resource_group.srch_rg.location
  sku                 = var.sku

  replica_count                 = 1
  partition_count               = 1
  public_network_access_enabled = false
  allowed_ips                   = [] # Lista di IP consentiti. //20.103.208.237 selc-d-aksoutbound-pip-01

  local_authentication_enabled = true
  authentication_failure_mode  = "http403"

  identity {
    type = "SystemAssigned"
  }

  tags = var.tags
}


resource "azurerm_key_vault_secret" "azure_search_api_key" {
  name         = "azure-search-api-key"
  value        = azurerm_search_service.srch_service.primary_key
  key_vault_id = data.azurerm_key_vault.key_vault.id
  content_type = "text/plain"
}