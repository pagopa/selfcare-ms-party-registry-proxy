resource "restapi_object" "search_index_ipa" {
  provider     = restapi.search
  query_string = "api-version=2023-11-01"
  id_attribute = "name"
  path         = "/indexes"

  data = jsonencode({
    "name" : "ipa-index",

    "analyzers" : [
      {
        "name" : "autocomplete_analyzer",
        "@odata.type" : "#Microsoft.Azure.Search.CustomAnalyzer",
        "tokenizer" : "autocomplete_tokenizer",
        "tokenFilters" : ["lowercase", "asciifolding"]
      },
      {
        "name" : "autocomplete_search_analyzer",
        "@odata.type" : "#Microsoft.Azure.Search.CustomAnalyzer",
        "tokenizer" : "lowercase",
        "tokenFilters" : ["lowercase", "asciifolding"]
      }
    ],

    "tokenizers" : [
      {
        "name" : "autocomplete_tokenizer",
        "@odata.type" : "#Microsoft.Azure.Search.EdgeNGramTokenizer",
        "minGram" : 3,
        "maxGram" : 10,
        "tokenChars" : ["letter", "digit"]
      }
    ],

    "fields" : [
      {
        "name" : "id",
        "type" : "Edm.String",
        "key" : true,
        "searchable" : false,
        "filterable" : true,
        "sortable" : true,
        "facetable" : false,
        "retrievable" : true
      },
      {
        "name" : "entityType",
        "type" : "Edm.String",
        "searchable" : false,
        "filterable" : true,
        "sortable" : true,
        "facetable" : false,
        "retrievable" : false
      },
      {
        "name" : "originId",
        "type" : "Edm.String",
        "searchable" : false,
        "filterable" : true,
        "sortable" : true,
        "facetable" : false,
        "retrievable" : false
      },
      {
        "name" : "description",
        "type" : "Edm.String",
        "searchable" : true,
        "filterable" : false,
        "sortable" : true,
        "facetable" : false,
        "retrievable" : true,
        "indexAnalyzer" : "autocomplete_analyzer",
        "searchAnalyzer" : "autocomplete_search_analyzer"
      },
      {
        "name" : "descriptionFull",
        "type" : "Edm.String",
        "searchable" : true,
        "filterable" : false,
        "sortable" : true,
        "facetable" : false,
        "retrievable" : true,
        "indexAnalyzer" : "autocomplete_analyzer",
        "searchAnalyzer" : "autocomplete_search_analyzer"
      },
      {
        "name" : "taxCode",
        "type" : "Edm.String",
        "searchable" : false,
        "filterable" : true,
        "sortable" : true,
        "facetable" : false,
        "retrievable" : false
      },
      {
        "name" : "category",
        "type" : "Edm.String",
        "searchable" : false,
        "filterable" : true,
        "sortable" : true,
        "facetable" : false,
        "retrievable" : false
      },
      {
        "name" : "digitalAddress",
        "type" : "Edm.String",
        "searchable" : false,
        "filterable" : false,
        "sortable" : false,
        "facetable" : false,
        "retrievable" : false
      },
      {
        "name" : "address",
        "type" : "Edm.String",
        "searchable" : true,
        "filterable" : false,
        "sortable" : false,
        "facetable" : false,
        "retrievable" : false,
        "analyzer" : "standard.lucene"
      },
      {
        "name" : "zipCode",
        "type" : "Edm.String",
        "searchable" : false,
        "filterable" : true,
        "sortable" : true,
        "facetable" : false,
        "retrievable" : false
      },
      {
        "name" : "origin",
        "type" : "Edm.String",
        "searchable" : false,
        "filterable" : true,
        "sortable" : true,
        "facetable" : false,
        "retrievable" : false
      },
      {
        "name" : "istatCode",
        "type" : "Edm.String",
        "searchable" : false,
        "filterable" : true,
        "sortable" : true,
        "facetable" : false,
        "retrievable" : false
      },
      {
        "name" : "o",
        "type" : "Edm.String",
        "searchable" : false,
        "filterable" : true,
        "sortable" : true,
        "facetable" : false,
        "retrievable" : false
      },
      {
        "name" : "ou",
        "type" : "Edm.String",
        "searchable" : false,
        "filterable" : true,
        "sortable" : true,
        "facetable" : false,
        "retrievable" : false
      },
      {
        "name" : "aoo",
        "type" : "Edm.String",
        "searchable" : false,
        "filterable" : true,
        "sortable" : true,
        "facetable" : false,
        "retrievable" : false
      }
    ]
  })

  depends_on = [
    azurerm_search_service.srch_service,
    azurerm_role_assignment.admins_group_to_ai_search_reader,
    azurerm_role_assignment.developers_group_to_ai_search_reader,
    azurerm_role_assignment.infra_ci_to_ai_search_service_contributor,
    azurerm_role_assignment.infra_cd_to_ai_search_service_contributor
  ]
}
