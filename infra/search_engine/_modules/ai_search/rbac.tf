resource "azurerm_role_assignment" "search_to_cosmodb" {
  scope                = data.azurerm_cosmosdb_account.cosmosdb.id
  role_definition_name = "Cosmos DB Account Reader Role"
  principal_id         = azurerm_search_service.search_engine_service.identity[0].principal_id
}

# resource "azurerm_cosmosdb_mongo_role_definition" "search_to_cosmos_data_reader_read_only_role" {
#   role_name                = "${var.project}-ReadOnlyRole"
#   cosmos_mongo_database_id = data.azurerm_cosmosdb_mongo_database.cosmosdb_database.id
#
#   privilege {
#     actions = [
#       "find",
#       "listCollections",
#       "listIndexes"
#     ]
#
#     resource {
#       collection_name = var.collection_name
#       db_name         = var.database_name
#     }
#   }
# }
#
# resource "azurerm_role_assignment" "search_service_mongo_role" {
#   role_definition_id = azurerm_cosmosdb_mongo_role_definition.search_to_cosmos_data_reader_read_only_role.id
#   principal_id       = azurerm_search_service.search_engine_service.identity[0].principal_id
#   scope              = data.azurerm_cosmosdb_account.cosmosdb.id
# }

# resource "azurerm_cosmosdb_sql_role_assignment" "search_to_cosmos_data_reader_db" {
#   resource_group_name = data.azurerm_cosmosdb_account.cosmosdb.resource_group_name
#   account_name        = data.azurerm_cosmosdb_account.cosmosdb.name
#   role_definition_id  = "${data.azurerm_cosmosdb_account.cosmosdb.id}/sqlRoleDefinitions/00000000-0000-0000-0000-000000000001"
#   principal_id        = azurerm_search_service.search_engine_service.identity[0].principal_id
#   scope               = "${data.azurerm_cosmosdb_account.cosmosdb.id}/dbs/${var.database_name}"
# }
#
# resource "azurerm_cosmosdb_sql_role_assignment" "search_to_cosmos_data_reader_db_colls" {
#   resource_group_name = data.azurerm_cosmosdb_account.cosmos.resource_group_name
#   account_name        = data.azurerm_cosmosdb_account.cosmos.name
#   role_definition_id  = "${data.azurerm_cosmosdb_account.cosmos.id}/sqlRoleDefinitions/00000000-0000-0000-0000-000000000001"
#   principal_id        = azurerm_search_service.srch.identity[0].principal_id
#   scope               = "${data.azurerm_cosmosdb_account.cosmos.id}/dbs/${var.database_name}/colls/services-publication"
# }

resource "azurerm_role_assignment" "admins_group_to_ai_search_reader" {
  scope                = azurerm_search_service.search_engine_service.id
  role_definition_name = "Search Index Data Reader"
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