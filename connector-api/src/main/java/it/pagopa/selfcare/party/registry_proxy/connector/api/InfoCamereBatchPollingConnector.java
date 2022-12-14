package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchPolling;

import java.util.List;


public interface InfoCamereBatchPollingConnector {

    InfoCamereBatchPolling save(InfoCamereBatchPolling infoCamereBatchPolling);
    List<InfoCamereBatchPolling> findByStatus(String status);

}
