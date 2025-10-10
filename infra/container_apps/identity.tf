resource "azurerm_role_assignment" "blob_state_access" {
  scope                = data.azurerm_storage_account.existing_logs_storage.id
  role_definition_name = "Storage Blob Data Contributor"
  principal_id         = data.azurerm_user_assigned_identity.cae_identity.principal_id
}