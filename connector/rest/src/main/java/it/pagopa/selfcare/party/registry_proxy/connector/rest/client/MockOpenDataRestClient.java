package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "${rest-client.mock-open-data.serviceCode}", url = "${rest-client.mock-open-data.base-url}")
//@Conditional(OpenDataMockEnabledCondition.class)
public interface MockOpenDataRestClient extends OpenDataRestClient {

    @GetMapping(value = "${rest-client.mock-open-data.retrieveInstitutions.path}", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    String retrieveInstitutions(@RequestParam("bom") String bom);

    @GetMapping(value = "${rest-client.mock-open-data.retrieveCategories.path}", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    String retrieveCategories(@RequestParam("bom") String bom);

}
