package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchRequest;

import java.util.List;

public interface InfoCamereBatchRequestConnector {
    InfoCamereBatchRequest save(InfoCamereBatchRequest infoCamereBatchRequest);
    List<InfoCamereBatchRequest> findAllByBatchId(String batchId);
    List<InfoCamereBatchRequest> findAllByBatchIdNotAndStatusWorking(String batchId);
    List<InfoCamereBatchRequest> setBatchIdAndStatusWorking(List<InfoCamereBatchRequest> infoCamereBatchRequests, String batchId);
}
