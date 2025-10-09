resource "azurerm_resource_group" "storage_rg" {
  name     = "${local.project}-dapr-storage-rg"
  location = var.location
  tags     = var.tags
}

resource "azurerm_storage_account" "dapr_storage" {
  name                     = "${replace(local.project, "-", "")}${local.pnpg_suffix}daprstorage"
  resource_group_name      = azurerm_resource_group.storage_rg.name
  location                 = azurerm_resource_group.storage_rg.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
  tags                     = var.tags
}

resource "azurerm_storage_container" "dapr_container" {
  name                  = "dapr-state"
  storage_account_id    = azurerm_storage_account.dapr_storage.id
  container_access_type = "private"
}

resource "azurerm_container_app_environment_dapr_component" "blob_state" {
  name                         = "blobstorage-state"
  #container_app_environment_id = data.azurerm_container_app_environment.cae.id
  container_app_environment_id = var.cae_id
  component_type               = "state.azure.blobstorage"
  version                      = "v1"

  metadata {
    name  = "authMode"
    value = "azureAD"
  }

  metadata {
    name  = "azureClientId"
    value = data.azurerm_user_assigned_identity.cae_identity.client_id
  }

  metadata {
    name  = "containerName"
    value = azurerm_storage_container.dapr_container.name
  }

}