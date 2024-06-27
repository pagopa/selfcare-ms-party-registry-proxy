terraform {
  required_version = ">= 1.6.0"

  backend "azurerm" {}
}

provider "azurerm" {
  features {}
}

module "container_app_party_reg_proxy" {
  source = "github.com/pagopa/selfcare-commons//infra/terraform-modules/container_app_microservice?ref=main"

  is_pnpg = var.is_pnpg

  env_short                      = var.env_short
  resource_group_name            = local.ca_resource_group_name
  container_app                  = var.container_app
  container_app_name             = "party-reg-proxy"
  container_app_environment_name = local.container_app_environment_name
  image_name                     = "selfcare-ms-party-registry-proxy"
  image_tag                      = var.image_tag
  app_settings                   = var.app_settings
  secrets_names                  = var.secrets_names
  workload_profile_name          = var.workload_profile_name
  probes = [
    {
      httpGet = {
        path   = "actuator/health"
        port   = 8080
        scheme = "HTTP"
      }
      timeoutSeconds      = 30
      type                = "Liveness"
      failureThreshold    = 3
      initialDelaySeconds = 1
    },
    {
      httpGet = {
        path   = "actuator/health"
        port   = 8080
        scheme = "HTTP"
      }
      timeoutSeconds      = 30
      type                = "Readiness"
      failureThreshold    = 30
      initialDelaySeconds = 30
    },
    {
      httpGet = {
        path   = "actuator/health"
        port   = 8080
        scheme = "HTTP"
      }
      timeoutSeconds      = 30
      failureThreshold    = 30
      type                = "Startup"
      initialDelaySeconds = 60
    }
  ]

  tags = var.tags
}
