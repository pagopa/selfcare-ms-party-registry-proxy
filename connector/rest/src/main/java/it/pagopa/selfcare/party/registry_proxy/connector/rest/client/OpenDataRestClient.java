package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

public interface OpenDataRestClient {

    String retrieveInstitutions(String bom);

    String retrieveCategories(String bom);

}
