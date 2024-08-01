is_pnpg          = true
env_short        = "u"
suffix_increment = "-001"
cae_name         = "cae-001"

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
    value = "https://selcuweupnpgcheckoutsa.z6.web.core.windows.net/resources"
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
    name  = "INFO_CAMERE_URL"
    value = "https://icapiscl.infocamere.it"
  },
  {
    name  = "INFO_CAMERE_INSTITUTIONS_BY_LEGAL_ENDPOINT"
    value = "/ic/ce/wspa/wspa/rest/listaLegaleRappresentante/{taxId}"
  },
  {
    name  = "INFO_CAMERE_AUTHENTICATION_ENDPOINT"
    value = "/ic/ce/wspa/wspa/rest/authentication"
  },
  {
    name  = "NATIONAL_REGISTRIES_URL"
    value = "https://api-selcpg.uat.notifichedigitali.it/national-registries-private"
  },
  {
    name  = "PDND_BASE_URL"
    value = "https://auth.interop.pagopa.it"
  },
  {
    name  = "PDND_INFOCAMERE_AUDIENCE"
    value = "auth.interop.pagopa.it/client-assertion"
  },
  {
    name  = "IVASS_BASE_URL"
    value = "https://infostat-ivass.bancaditalia.it/RIGAInquiry-public"
  }
]

secrets_names = {
  "BLOB_STORAGE_CONN_STRING"              = "web-storage-connection-string"
  "NATIONAL_REGISTRY_API_KEY"             = "national-registry-api-key"
  "APPLICATIONINSIGHTS_CONNECTION_STRING" = "appinsights-connection-string"
  "JWT_TOKEN_PUBLIC_KEY"                  = "jwt-public-key"
  "PDND_INFOCAMERE_PRIVATE_KEY"           = "infocamere-interop-certificate-client-private-key"
  "PDND_INFOCAMERE_CLIENT_ID"             = "infocamere-interop-client-id"
  "PDND_INFOCAMERE_KID"                   = "infocamere-interop-kid"
  "PDND_INFOCAMERE_PURPOSE_ID"            = "infocamere-interop-purpose-id"
}
