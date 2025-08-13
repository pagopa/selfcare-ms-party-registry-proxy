resource "azurerm_resource_group" "search_engine_rg" {
  name     = "${var.project}-search-engine-rg"
  location = var.location

  tags = var.tags
}
# Azure AI Search Service
resource "azurerm_search_service" "search_engine_service" {
  name                = "${var.project}-search-service"
  resource_group_name = azurerm_resource_group.search_engine_rg.name
  location            = azurerm_resource_group.search_engine_rg.location
  sku                 = var.sku

  # Configurazioni opzionali
  replica_count                 = 1
  partition_count               = 1
  public_network_access_enabled = true
  allowed_ips                   = [] # Lista di IP consentiti

  local_authentication_enabled = true
  authentication_failure_mode  = "http403"

  identity {
    type = "SystemAssigned"
  }

  tags = var.tags
}
