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

  env_short          = var.env_short
  container_app      = var.container_app
  container_app_name = "party-reg-proxy"
  image_name         = "selfcare-ms-party-registry-proxy"
  image_tag          = var.image_tag
  app_settings       = var.app_settings
  secrets_names      = var.secrets_names

  tags = var.tags
}
