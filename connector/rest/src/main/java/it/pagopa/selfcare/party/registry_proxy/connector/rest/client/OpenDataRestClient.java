package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import org.springframework.web.bind.annotation.RequestParam;

public interface OpenDataRestClient {

    String retrieveInstitutions(@RequestParam("bom") String bom);

    String retrieveCategories(@RequestParam("bom") String bom);

}
