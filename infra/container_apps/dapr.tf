resource "azurerm_resource_group" "storage_rg" {
  name     = "${local.project}-dapr-storage-rg"
  location = var.location
  tags     = var.tags
}

resource "azurerm_storage_account" "dapr_storage" {
  name                      = "${replace(local.project, "-", "")}${local.pnpg_suffix}daprstorage"
  resource_group_name       = azurerm_resource_group.storage_rg.name
  location                  = azurerm_resource_group.storage_rg.location
  account_tier              = "Standard"
  account_replication_type  = "LRS"
  enable_https_traffic_only = true
  tags                      = var.tags
}

variable "ca_name" {
  type        = string
  description = "Container App name"
  default     = "cae-cp"
}

variable "ca_rg_name" {
  type        = string
  description = "Container App Resource group name"
  default     = "cae-rg"
}

data "azurerm_container_app" "ca" {
  name                = var.ca_name
  resource_group_name = var.ca_rg_name
}

data "azurerm_container_app_environment" "cae" {
  name                = var.cae_name
  resource_group_name = var.cae_rg_name
}

resource "azurerm_storage_container" "dapr_container" {
  name                  = "dapr-state"
  storage_account_name  = azurerm_storage_account.dapr_storage.name
  container_access_type = "private"
}

resource "azurerm_container_app_environment_dapr_component" "blob_state" {
  name                         = "blobstorage-state"
  container_app_environment_id = data.azurerm_container_app_environment.cae.id
  component_type               = "state.azure.blobstorage"
  version                      = "v1"

  metadata {
    name  = "authMode"
    value = "azureAD"
  }

  metadata {
    name  = "azureClientId"
    value = data.azurerm_user_assigned_identity.cae_identity.client_id
  }

  metadata {
    name  = "containerName"
    value = azurerm_storage_container.dapr_container.name
  }

  scopes = [data.azurerm_container_app.ca.name]

}
