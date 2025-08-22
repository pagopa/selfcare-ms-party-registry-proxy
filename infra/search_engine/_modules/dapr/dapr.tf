resource "azurerm_container_app_environment_dapr_component" "eventhub_pubsub" {
  name                         = "${var.prefix}-eventhub-pubsub"
  container_app_environment_id = data.azurerm_container_app_environment.cae.id
  component_type               = "pubsub.kafka"
  version                      = "v1"

  metadata {
    name  = "authType"
    value = "password"
  }

  metadata {
    name  = "saslUsername"
    value = "$ConnectionString"
  }

  metadata {
    name  = "saslPassword"
    value = "Endpoint=sb://${var.queue_url};SharedAccessKeyName=${var.queue_consumer_group};SharedAccessKey=${data.azurerm_key_vault_secret.event_hub_consumer_key.value};EntityPath=${var.queue_topic}"
  }

  metadata {
    name  = "consumerGroup"
    value = var.queue_consumer_group
  }

  metadata {
    name  = "brokers"
    value = "${var.queue_url}:${var.queue_port}"
  }

  scopes = [data.azurerm_container_app.ca.dapr[0].app_id]
}

resource "azurerm_container_app_environment_dapr_component" "http_search_binding" {
  name                         = "${var.prefix}-http-search-binding"
  container_app_environment_id = data.azurerm_container_app_environment.cae.id
  component_type               = "bindings.http"
  version                      = "v1"

  metadata {
    name  = "url"
    value = "https://${var.search_service_name}.search.windows.net/indexes/institution-index/docs/index?api-version=2023-11-01"
  }

  metadata {
    name  = "headers"
    value = jsonencode({
      "api-key"      = var.search_service_key
      "Content-Type" = "application/json"
    })
  }

  scopes = [data.azurerm_container_app.ca.dapr[0].app_id]
}

resource "azurerm_resource_group" "redis_rg" {
  name     = "${var.project}-dapr-redis-rg"
  location = var.location
  tags     = var.tags
}

resource "azurerm_redis_cache" "redis_cache" {
  count               = var.env_short == "d" ? 1 : 0
  name                = "${var.project}-dapr-redis"
  location            = var.location
  resource_group_name = "${var.project}-dapr-redis-rg"
  capacity            = 0
  family              = "C"
  sku_name            = "Basic"

  tags = var.tags
}

resource "azurerm_container_app_environment_dapr_component" "redis_state" {
  count                        = var.env_short == "d" ? 1 : 0
  name                         = "redis-state"
  container_app_environment_id = data.azurerm_container_app_environment.cae.id
  component_type               = "state.redis"
  version                      = "v1"

  metadata {
    name  = "redisHost"
    value = "${azurerm_redis_cache.redis_cache[0].hostname}:${azurerm_redis_cache.redis_cache[0].ssl_port}"
  }

  metadata {
    name  = "redisPassword"
    value = azurerm_redis_cache.redis_cache[0].primary_access_key
  }

  metadata {
    name  = "enableTLS"
    value = "true"
  }

  scopes = [data.azurerm_container_app.ca.dapr[0].app_id]
}

# Secrets per Dapr Components
resource "azurerm_container_app_environment_dapr_component" "secrets" {
  name                         = "${var.project}-secrets"
  container_app_environment_id = data.azurerm_container_app_environment.cae.id
  component_type               = "secretstores.azure.keyvault"
  version                      = "v1"

  metadata {
    name  = "vaultName"
    value = data.azurerm_key_vault.key_vault.name
  }

  metadata {
    name  = "azureClientId"
    value = data.azurerm_user_assigned_identity.cae_identity.client_id
  }

  scopes = [data.azurerm_container_app.ca.dapr[0].app_id]
}