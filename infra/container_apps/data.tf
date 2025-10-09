data "azurerm_container_app_environment" "cae" {
  name                = var.cae_name
  resource_group_name = var.cae_rg_name
}

data "azurerm_container_app" "ca" {
  name                = var.ca_name
  resource_group_name = var.ca_rg_name
}