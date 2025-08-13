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

  project = local.project
  location = local.location
  sku = local.sku
  tags = local.tags

  cosmosdb_prefix = local.cosmosdb_prefix
  database_name = local.database_name
  collection_name = local.collection_name

}