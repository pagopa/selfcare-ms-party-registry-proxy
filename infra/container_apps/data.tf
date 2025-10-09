data "azurerm_container_app_environment" "cae" {
  name                = var.cae_name
  resource_group_name = var.cae_rg_name
}

data "azurerm_storage_account" "existing_logs_storage" {
  name                = "selcdstlogs"
  resource_group_name = var.existing_logs_rg
}

data "azurerm_key_vault" "key_vault" {
  resource_group_name = var.key_vault_resource_group_name
  name                = var.key_vault_name
}

data "azurerm_key_vault_secret" "logs_storage_access_key" {
  name         = "logs-storage-access-key"
  key_vault_id = data.azurerm_key_vault.key_vault.id
}