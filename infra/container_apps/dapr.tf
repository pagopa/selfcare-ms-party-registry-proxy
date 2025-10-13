resource "azurerm_storage_container" "visura" {
  name                  = "visura"
  storage_account_id    = data.azurerm_storage_account.existing_logs_storage.id
  container_access_type = "private"
}

resource "azurerm_container_app_environment_dapr_component" "blob_state" {
  count                        = var.is_pnpg  ? 0 : 1
  name                         = "blobstorage-state"
  container_app_environment_id = data.azurerm_container_app_environment.cae.id
  component_type               = "state.azure.blobstorage"
  version                      = "v1"

  metadata {
    name  = "accountName"
    value = data.azurerm_storage_account.existing_logs_storage.name
  }

  metadata {
    name  = "containerName"
    value = azurerm_storage_container.visura.name
  }

  metadata {
    name  = "azureClientId"
    value = data.azurerm_user_assigned_identity.cae_identity.client_id
  }

  scopes = [data.azurerm_container_app.ca.dapr[0].app_id]

  lifecycle {
    prevent_destroy = false
  }
}