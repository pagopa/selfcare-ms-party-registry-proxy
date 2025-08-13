resource "azurerm_resource_group" "search_engine_rg" {
  name     = "${var.project}-search-engine-rg"
  location = var.location

  tags = var.tags
}
# Azure AI Search Service
resource "azurerm_search_service" "search_engine_service" {
  name                = "${var.project}-search-service"
  resource_group_name = azurerm_resource_group.search_engine_rg.name
  location            = azurerm_resource_group.search_engine_rg.location
  sku                 = var.sku

  # Configurazioni opzionali
  replica_count                 = 1
  partition_count               = 1
  public_network_access_enabled = true
  allowed_ips                   = [] # Lista di IP consentiti

  # Configurazioni di sicurezza
  local_authentication_enabled = true
  authentication_failure_mode  = "http401WithBearerChallenge"

  # Identity per l'integrazione con altri servizi Azure
  identity {
    type = "SystemAssigned"
  }

  tags = var.tags
}

# Outputs utili
output "search_service_name" {
  value = azurerm_search_service.search_engine_service.name
}

output "search_service_url" {
  value = "https://${azurerm_search_service.search_engine_service.name}.search.windows.net"
}

output "search_service_admin_key" {
  value     = azurerm_search_service.search_engine_service.primary_key
  sensitive = true
}

output "search_service_query_key" {
  value     = azurerm_search_service.search_engine_service.query_keys[0].key
  sensitive = true
}

output "resource_group_name" {
  value = azurerm_resource_group.search_engine_rg.name
}

# Esempio di configurazione per un indice personalizzato tramite REST API
# Nota: Terraform non ha un provider nativo per gli indici di Azure AI Search,
# quindi utilizziamo il provider 'restapi' o script esterni


# Configurazione del provider REST API
provider "restapi" {
  alias = "search"
  uri   = "https://${azurerm_search_service.search_engine_service.name}.search.windows.net"

  headers = {
    "api-key"      = azurerm_search_service.search_engine_service.primary_key
    "Content-Type" = "application/json"
  }

  write_returns_object = true
  create_method        = "POST"
  update_method        = "PUT"
  destroy_method       = "DELETE"
}

# Definizione dell'indice personalizzato
resource "restapi_object" "search_index" {
  provider   = restapi.search
  query_string = "api-version=2023-11-01"
  id_attribute = "name"
  path         = "/indexes"

  data      = jsonencode({
    "name": "institution-index",
    "fields": [
      {
        "name": "id",
        "type": "Edm.String",
        "key": true,
        "searchable": false,
        "filterable": true,
        "sortable": true,
        "facetable": false,
        "retrievable": true
      },
      {
        "name": "description",
        "type": "Edm.String",
        "key": false,
        "searchable": true,
        "filterable": false,
        "sortable": true,
        "facetable": false,
        "retrievable": true,
        "analyzer": "it.microsoft"
      },
      {
        "name": "taxCode",
        "type": "Edm.String",
        "searchable": true,
        "filterable": true,
        "sortable": true,
        "facetable": false,
        "analyzer": "standard.lucene"
      },
      {
        "name": "products",
        "type": "Collection(Edm.String)",
        "retrievable": true,
        "searchable": true,
        "filterable": true,
        "sortable": false,
        "facetable": true
      },{
        "name": "institutionTypes",
        "type": "Collection(Edm.String)",
        "retrievable": true,
        "searchable": true,
        "filterable": true,
        "sortable": false,
        "facetable": true
      },
      # {
      #   "name": "lastModified",
      #   "type": "Edm.DateTimeOffset",
      #   "retrievable": true,
      #   "filterable": true,
      #   "sortable": true
      # }
      {
        name         = "systemLastModified"
        type         = "Edm.DateTimeOffset"
        searchable   = false
        filterable   = true
        sortable     = true
        facetable    = false
        retrievable  = true
      },
    ]
  })

  depends_on = [azurerm_search_service.search_engine_service]
}

data "azurerm_cosmosdb_account" "cosmosdb" {
  name                = "${var.cosmosdb_prefix}-cosmosdb-mongodb-account"
  resource_group_name = "${var.cosmosdb_prefix}-cosmosdb-mongodb-rg"
}

resource "azurerm_role_assignment" "search_to_cosmos" {
  scope                = data.azurerm_cosmosdb_account.cosmosdb.id
  role_definition_name = "Cosmos DB Account Reader Role"
  principal_id         = azurerm_search_service.search_engine_service.identity[0].principal_id
}

resource "restapi_object" "search_datasource" {
  provider = restapi.search
  path         = "/datasources"
  query_string = "api-version=2023-11-01"
  id_attribute = "name"

  data = jsonencode({
    name = "${var.project}-cosmosdb-aisearch-datasource"
    type = "cosmosdb"
    credentials = {
      connectionString = "ResourceId=${data.azurerm_cosmosdb_account.cosmosdb.id};Database=${var.database_name};"
    }
    container = {
      name = var.collection_name
    }
    dataChangeDetectionPolicy = {
      "@odata.type"           = "#Microsoft.Azure.Search.HighWaterMarkChangeDetectionPolicy"
      highWaterMarkColumnName = "_ts" # Campo timestamp di Cosmos DB per il tracciamento delle modifiche
    }
    dataDeletionDetectionPolicy = {
      "@odata.type"         = "#Microsoft.Azure.Search.SoftDeleteColumnDeletionDetectionPolicy"
      softDeleteColumnName  = "isDeleted"
      softDeleteMarkerValue = "true"
    }
  })

  depends_on = [
    azurerm_search_service.search_engine_service,
    azurerm_role_assignment.search_to_cosmos
  ]
}

# 2. Crea l'Indexer
resource "restapi_object" "search_indexer" {
  provider = restapi.search

  path         = "/indexers"
  query_string = "api-version=2023-11-01"
  id_attribute = "name"


  data = jsonencode({
    name            = "${var.project}-cosmosdb-aisearch-indexer"
    dataSourceName  = "${var.project}-cosmosdb-aisearch-datasource"
    targetIndexName = restapi_object.search_index.id
    schedule = {
      interval = "PT5M" # Esegui l'indexer ogni ora
    }
    parameters = {
      configuration = {
        dataToExtract           = "contentAndMetadata"
        imageAction            = "none"
        parsingMode            = "default"
      }
      batchSize              = 50
      maxFailedItems         = 10
      maxFailedItemsPerBatch = 5
    }
    # Mappatura dei campi da MongoDB all'indice Azure AI Search
    fieldMappings = [
      {
        sourceFieldName = "_id"
        targetFieldName = "id"
        mappingFunction = {
          name = "base64Encode" # Codifica l'ObjectId di MongoDB
        }
      },
      {
        sourceFieldName = "description"
        targetFieldName = "description"
      },
      {
        sourceFieldName = "taxCode"
        targetFieldName = "taxCode"
      },
      {
        sourceFieldName = "_ts"
        targetFieldName = "systemLastModified"
        mappingFunction = {
          name = "timestampToDateTime"
        }
      }
    ]

    outputFieldMappings = [
      {
        sourceFieldName = "/document/productId"
        targetFieldName = "products"
      },
      {
        sourceFieldName = "/document/institutionType"
        targetFieldName = "institutionTypes"
      }
    ]
  })

  depends_on = [
    restapi_object.search_index,
    restapi_object.search_datasource
  ]
}