env_short             = "u"
private_dns_name      = "selc-u-party-reg-proxy-ca.agreeablesky-f71e6306.westeurope.azurecontainerapps.io"
dns_zone_prefix       = "uat.selfcare"
api_dns_zone_prefix   = "api.uat.selfcare"
cae_name              = "cae"
workload_profile_name = "Consumption"

tags = {
  CreatedBy   = "Terraform"
  Environment = "Uat"
  Owner       = "SelfCare"
  Source      = "https://github.com/pagopa/selfcare-ms-party-registry-proxy"
  CostCenter  = "TS310 - PAGAMENTI & SERVIZI"
}

container_app = {
  min_replicas = 1
  max_replicas = 2
  scale_rules  = []
  cpu          = 1
  memory       = "2Gi"
}

app_settings = [
  {
    name  = "JAVA_TOOL_OPTIONS"
    value = "-javaagent:applicationinsights-agent.jar"
  },
  {
    name  = "APPLICATIONINSIGHTS_INSTRUMENTATION_LOGGING_LEVEL"
    value = "OFF"
  },
  {
    name  = "MS_PARTY_REGISTRY_PROXY_LOG_LEVEL"
    value = "DEBUG"
  },
  {
    name  = "MOCK_OPEN_DATA_ENABLED"
    value = "false"
  },
  {
    name  = "MOCK_OPEN_DATA_URL"
    value = "https://selcucheckoutsa.z6.web.core.windows.net/resources"
  },
  {
    name  = "MOCK_OPEN_DATA_INSTITUTION_ENDPOINT"
    value = "/institutions-open-data-mock.csv"
  },
  {
    name  = "MOCK_OPEN_DATA_CATEGORY_ENDPOINT"
    value = "/categories-open-data-mock.csv"
  },
  {
    name  = "MOCK_OPEN_DATA_AOO_ENDPOINT"
    value = "/aoo-open-data-mock.csv"
  },
  {
    name  = "MOCK_OPEN_DATA_UO_ENDPOINT"
    value = "/uo-open-data-mock.csv"
  },
  {
    name  = "ANAC_FTP_IP"
    value = "93.43.119.85"
  },
  {
    name  = "ANAC_FTP_USER"
    value = "PagoPA_user"
  },
  {
    name  = "ANAC_FTP_DIRECTORY"
    value = "/mnt/RegistroGestoriPiattaforme/Collaudo/"
  },
  {
    name  = "LUCENE_INDEX_INSTITUTIONS_FOLDER"
    value = "index/institutions"
  },
  {
    name  = "LUCENE_INDEX_CATEGORIES_FOLDER"
    value = "index/categories"
  },
  {
    name  = "LUCENE_INDEX_AOOS_FOLDER"
    value = "index/aoos"
  },
  {
    name  = "LUCENE_INDEX_UOS_FOLDER"
    value = "index/uos"
  },
  {
    name  = "LUCENE_INDEX_ANAC_FOLDER"
    value = "index/anac"
  },
  {
    name  = "LUCENE_INDEX_IVASS_FOLDER"
    value = "index/ivass"
  }
]

secrets_names = {
  "BLOB_STORAGE_CONN_STRING"              = "web-storage-connection-string"
  "NATIONAL_REGISTRY_API_KEY"             = "national-registry-api-key"
  "APPLICATIONINSIGHTS_CONNECTION_STRING" = "appinsights-connection-string"
  "JWT_TOKEN_PUBLIC_KEY"                  = "jwt-public-key"
  "GEOTAXONOMY_API_KEY"                   = "geotaxonomy-api-key"
  "ANAC_FTP_PASSWORD"                     = "anac-ftp-password"
  "ANAC_FTP_KNOWN_HOST"                   = "anac-ftp-known-host"
}
