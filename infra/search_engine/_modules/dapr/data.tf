data "azurerm_container_app_environment" "cae" {
  name                = var.cae_name
  resource_group_name = var.cae_rg_name
}

data "azurerm_container_app" "ca" {
  name                = var.ca_name
  resource_group_name = var.ca_rg_name
}

data "azurerm_user_assigned_identity" "cae_identity" {
  name                = "${var.cae_name}-managed_identity"
  resource_group_name = var.cae_rg_name
}

data "azurerm_key_vault" "key_vault" {
  resource_group_name = var.key_vault_resource_group_name
  name                = var.key_vault_name
}

data "azurerm_key_vault_secret" "event_hub_consumer_key" {
  name         = var.key_vault_event_hub_consumer_key
  key_vault_id = data.azurerm_key_vault.key_vault.id
}