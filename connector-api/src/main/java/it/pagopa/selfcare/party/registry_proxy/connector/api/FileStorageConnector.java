package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.ResourceResponse;

public interface FileStorageConnector {

    ResourceResponse getFile(String fileName);

}
