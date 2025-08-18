locals {
  prefix           = "selc"
  env_short        = "d"
  location         = "westeurope"
  location_short   = "weu"
  domain           = "ar"
  project          = "${local.prefix}-${local.env_short}-${local.location_short}-${local.domain}"
  sku              = "basic"
  cosmosdb_prefix  = "${local.prefix}-${local.env_short}"
  database_name    = "selcMsCore"
  collection_name  = "Institution"
  
  key_vault_resource_group_name    = "selc-${local.env_short}-sec-rg"
  key_vault_name                   = "selc-${local.env_short}-kv"
  key_vault_cosmosdb_key           = "mongodb-connection-key"
  key_vault_event_hub_consumer_key = "eventhub-sc-contracts-selc-proxy-key-lc"

  tags = {
    CostCenter  = "TS310 - PAGAMENTI & SERVIZI"
    CreatedBy   = "Terraform"
    Environment = "Dev"
    Owner       = "SelfCare"
    Source      = "https://github.com/pagopa/selfcare-ms-party-registry-proxy/infra/search_engine"
  }
}
