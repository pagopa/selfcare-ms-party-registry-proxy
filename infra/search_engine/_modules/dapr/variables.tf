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

variable "cae_name" {
  type        = string
  description = "Container App Environment name"
  default     = "cae-cp"
}

variable "cae_rg_name" {
  type        = string
  description = "Container App Environment Resource group name"
  default     = "cae-rg"
}

variable "consumer_group" {
  type        = string
  description = "Eventhub consumer group"
  default     = "selc-proxy"
}

variable "env_short" {
  description = "Environment short name"
  default     = "d"
  type        = string
  validation {
    condition = (
      length(var.env_short) <= 1
    )
    error_message = "Max length is 1 chars."
  }
}


variable "key_vault_resource_group_name" {
  type        = string
  description = "Name of Key Vault resource group"
}

variable "key_vault_name" {
  type        = string
  description = "Name of Key Vault"
}

variable "key_vault_event_hub_consumer_key" {
  type        = string
  description = "Name of Key Vault"
}

variable "location" {
  type    = string
  default = "westeurope"
}

variable "prefix" {
  description = "Domain prefix"
  type        = string
  default     = "selc"
  validation {
    condition = (
      length(var.prefix) <= 6
    )
    error_message = "Max length is 6 chars."
  }
}

variable "project" {
  type        = string
  description = "Selfcare prefix and short environment"
}

variable "search_service_name" {
  type    = string
  description = "Name of ai search service"
}

variable "search_service_key" {
  type    = string
  description = "Key of ai search service"
}


variable "tags" {
  type = map(any)
}

