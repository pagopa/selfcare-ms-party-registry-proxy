output "search_service_name" {
  value = azurerm_search_service.search_engine_service.name
}

output "search_service_id" {
  value = azurerm_search_service.search_engine_service.id
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
