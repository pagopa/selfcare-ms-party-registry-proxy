package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchServiceStatus;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.IvassAzureSearchRestClientConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.IvassSearchServiceResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.SearchServiceIVASSRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@FeignClient(name = "${rest-client.ivass-ai-search.serviceCode}", url = "${rest-client.ivass-ai-search.base-url}", configuration = IvassAzureSearchRestClientConfig.class)
public interface IvassAzureSearchRestClient {

  @PostMapping(value = "${rest-client.ivass-ai-search.insurance-company.add.path}", consumes = APPLICATION_JSON_VALUE)
  @ResponseBody
  SearchServiceStatus indexInsuranceCompanyIVASS(@RequestBody SearchServiceIVASSRequest searchServiceRequest,
                                          @PathVariable("indexName") String indexName,
                                          @RequestParam("api-version") String apiVersion);

  @GetMapping("/indexes/{indexName}/docs")
  IvassSearchServiceResponse search(
          @PathVariable("indexName") String indexName,
          @RequestParam("api-version") String apiVersion,
          @RequestParam("search") String search,
          @RequestParam(value = "$searchFields", required = false) String searchFields,
          @RequestParam(value = "$filter", required = false) String filter,
          @RequestParam(value = "$skip", required = false) Integer skip,
          @RequestParam(value = "$top", required = false) Integer top,
          @RequestParam(value = "$count", required = false) Boolean count
  );
}