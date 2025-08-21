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

  key_vault_name                = "selc-${local.env_short}-kv"
  key_vault_resource_group_name = "selc-${local.env_short}-sec-rg"
  key_vault_cosmosdb_key        = "mongodb-connection-key"

  srch_private_endpoint_enabled = true
}

module "dapr" {
  source = "../../_modules/dapr"
    
  project   = local.project
  env_short = local.env_short

  cae_name    = "selc-${local.env_short}-cae-002"
  cae_rg_name = "selc-${local.env_short}-container-app-002-rg"
  ca_name     = "selc-${local.env_short}-party-reg-proxy-ca"
  ca_rg_name  = "selc-${local.env_short}-container-app-002-rg"

  key_vault_name                   = "selc-${local.env_short}-kv"
  key_vault_resource_group_name    = "selc-${local.env_short}-sec-rg"
  key_vault_event_hub_consumer_key = "eventhub-sc-contracts-selc-proxy-connection-string-lc"

  queue_url = "selc-${local.env_short}-eventhub-ns.servicebus.windows.net:9093"

  search_service_name = module.ai_search.search_service_name
  search_service_key  = module.ai_search.search_service_admin_key

  tags = local.tags

}