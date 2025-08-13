locals {
  prefix         = "selc"
  env_short      = "d"
  location       = "westeurope"
  location_short = "weu"
  project        = "${local.prefix}-${local.env_short}-${local.location_short}"
  sku            = "free"
  cosmosdb_prefix  = "${local.prefix}-${local.env_short}"
  database_name = "Institution"
  collection_name = "selcMsCore"

  tags = {
    CostCenter  = "TS310 - PAGAMENTI & SERVIZI"
    CreatedBy   = "Terraform"
    Environment = "Dev"
    Owner       = "SelfCare"
    Source      = "https://github.com/pagopa/selfcare-ms-party-registry-proxy/infra/search_engine"
  }
}
