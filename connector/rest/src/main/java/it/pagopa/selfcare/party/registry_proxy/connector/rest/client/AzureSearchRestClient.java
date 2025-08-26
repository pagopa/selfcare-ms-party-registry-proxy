package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import it.pagopa.selfcare.party.registry_proxy.connector.model.AzureSearchStatus;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InstitutionIndexValue;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.AzureSearchRestClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.ai-search.serviceCode}", url = "${rest-client.ai-search.base-url}", configuration = AzureSearchRestClientConfig.class)
public interface AzureSearchRestClient {
  @PostMapping(value = "${rest-client.ai-search.institution.path}", consumes = APPLICATION_JSON_VALUE)
  @ResponseBody
  AzureSearchStatus indexInstitution(@RequestBody InstitutionIndexValue institutionIndexValue);
}
