terraform {

  backend "azurerm" {
    resource_group_name  = "terraform-state-rg"
    storage_account_name = "tfappdevselfcare"
    container_name       = "terraform-state"
    key                  = "selfcare-party-registry-proxy.search_engine.tfstate"
  }
}

provider "azurerm" {
  features {}
}


module "ai_search" {
  source = "../../_modules/ai_search"
  app_name = "${local.prefix}-srch"
  prefix = local.prefix
  env_short = local.env_short
  project = local.project
  location = local.location
  sku = local.sku
  tags = local.tags
  cidr_subnet = ["10.1.145.0/29"]

  cosmosdb_prefix = local.cosmosdb_prefix
  database_name   = local.database_name
  collection_name = local.collection_name

  key_vault_name                = local.key_vault_name
  key_vault_resource_group_name = local.key_vault_resource_group_name
  key_vault_cosmosdb_key        = local.key_vault_cosmosdb_key
}