resource "azurerm_storage_container" "visura" {
  name                  = "visura"
  storage_account_id    = data.azurerm_storage_account.existing_logs_storage.id
  container_access_type = "private"
}

resource "azurerm_container_app_environment_dapr_component" "blob_state" {
  name                         = "blobstorage-state"
  container_app_environment_id = data.azurerm_container_app_environment.cae.id
  component_type               = "state.azure.blobstorage"
  version                      = "v1"

  metadata {
    name  = "accountName"
    value = data.azurerm_storage_account.existing_logs_storage.name
  }

  metadata {
    name  = "accountKey"
    value = data.azurerm_key_vault_secret.logs_storage_access_key.value
  }

  metadata {
    name  = "containerName"
    value = azurerm_storage_container.visura.name
  }

  scopes = [data.azurerm_container_app.ca.dapr[0].app_id]

}

resource "azurerm_container_app_environment_dapr_component" "secrets" {
  name                         = "${var.project_domain}-secrets"
  container_app_environment_id = data.azurerm_container_app_environment.cae.id
  component_type               = "secretstores.azure.keyvault"
  version                      = "v1"

  metadata {
    name  = "vaultName"
    value = data.azurerm_key_vault.key_vault.name
  }

  metadata {
    name  = "azureClientId"
    value = data.azurerm_user_assigned_identity.cae_identity.client_id
  }

  scopes = [data.azurerm_container_app.ca.dapr[0].app_id]
}
