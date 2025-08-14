# resource "azurerm_role_assignment" "identity_to_cosmodb" {
#   scope                = data.azurerm_cosmosdb_account.cosmosdb.id
#   role_definition_name = "Cosmos DB Account Reader Role"
#   principal_id         = azurerm_search_service.search_engine_service.identity[0].principal_id
# }


resource "azurerm_role_assignment" "admins_group_to_ai_search_reader" {
  scope                = azurerm_search_service.search_engine_service.id
  role_definition_name = "Search Service Contributor"
  principal_id         = data.azuread_group.adgroup_admin.object_id
}

resource "azurerm_role_assignment" "developers_group_to_ai_search_reader" {
  scope                = azurerm_search_service.search_engine_service.id
  role_definition_name = "Search Index Data Reader"
  principal_id         = data.azuread_group.adgroup_developers.object_id
}

# resource "azurerm_role_assignment" "infra_ci_to_ai_search_service_contributor" {
#   scope                = azurerm_search_service.search_engine_service.id
#   role_definition_name = "Search Service Contributor"
#   principal_id         = data.azurerm_user_assigned_identity.managed_identity_infra_ci.principal_id
# }

# resource "azurerm_role_assignment" "infra_cd_to_ai_search_service_contributor" {
#   scope                = azurerm_search_service.search_engine_service.id
#   role_definition_name = "Search Service Contributor"
#   principal_id         = data.azurerm_user_assigned_identity.managed_identity_infra_cd.principal_id
# }