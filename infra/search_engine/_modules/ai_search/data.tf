data "azurerm_subscription" "current" {}

data "azurerm_resource_group" "rg_vnet" {
  name = "${var.prefix}-${var.env_short}-vnet-rg"
}

data "azurerm_virtual_network" "vnet" {
  name                = "${var.prefix}-${var.env_short}-vnet"
  resource_group_name = data.azurerm_resource_group.rg_vnet.name
}

data "azurerm_key_vault" "key_vault" {
  resource_group_name = var.key_vault_resource_group_name
  name                = var.key_vault_name
}

data "azurerm_key_vault_secret" "cosmosdb_connection_key" {
  name         = var.key_vault_cosmosdb_key
  key_vault_id = data.azurerm_key_vault.key_vault.id
}

data "azurerm_cosmosdb_account" "cosmosdb" {
  name                = "${var.cosmosdb_prefix}-cosmosdb-mongodb-account"
  resource_group_name = "${var.cosmosdb_prefix}-cosmosdb-mongodb-rg"
}

data "azurerm_cosmosdb_mongo_database" "cosmosdb_database" {
  name                = var.database_name
  resource_group_name = data.azurerm_cosmosdb_account.cosmosdb.resource_group_name
  account_name        = data.azurerm_cosmosdb_account.cosmosdb.name
}

data "azuread_group" "adgroup_admin" {
  display_name = "${var.prefix}-${var.env_short}-adgroup-admin"
}

data "azuread_group" "adgroup_developers" {
  display_name = "${var.prefix}-${var.env_short}-adgroup-developers"
}

data "azurerm_private_dns_zone" "cosmosdb" {
  name                = "privatelink.mongo.cosmos.azure.com"
  resource_group_name = "${var.prefix}-${var.env_short}-vnet-rg"
}

data "azurerm_subnet" "cosmosdb" {
  name                 = "${var.prefix}-${var.env_short}-cosmosb-mongodb-snet"
  virtual_network_name = data.azurerm_virtual_network.vnet.name
  resource_group_name  = "${var.prefix}-${var.env_short}-vnet-rg"
}


# data "azurerm_user_assigned_identity" "managed_identity_infra_ci" {
#   name                = "${var.prefix}-${var.env_short}-ms-github-ci-identity"
#   resource_group_name = azurerm_resource_group.search_engine_rg.name
# }
#
# data "azurerm_user_assigned_identity" "managed_identity_infra_cd" {
#   name                = "${var.prefix}-${var.env_short}-ms-github-cd-identity"
#   resource_group_name = azurerm_resource_group.search_engine_rg.name
# }