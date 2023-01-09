package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereBatchPollingConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereBatchRequestConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InfoCamereBatchRequestServiceImpl implements InfoCamereBatchRequestService {

    private final InfoCamereBatchRequestConnector infoCamereBatchRequestConnector;
    private final InfoCamereBatchPollingConnector infoCamereBatchPollingConnector;
    private final InfoCamereConnector infoCamereConnector;

    public InfoCamereBatchRequestServiceImpl(
            InfoCamereBatchRequestConnector infoCamereBatchRequestConnector,
            InfoCamereBatchPollingConnector infoCamereBatchPollingConnector,
            InfoCamereConnector infoCamereConnector) {
        this.infoCamereBatchRequestConnector = infoCamereBatchRequestConnector;
        this.infoCamereBatchPollingConnector = infoCamereBatchPollingConnector;
        this.infoCamereConnector = infoCamereConnector;
    }

    @Override
    public void batchPecListRequest() {
        log.trace("batchPecListRequest start");
        boolean hasNext = true;
        while(hasNext){
            hasNext = false;
            List<InfoCamereBatchRequest> infoCamereBatchRequests = infoCamereBatchRequestConnector.findAllByBatchId(BatchStatus.NO_BATCH_ID.getValue());
            log.info("Found Batch Requests size: {} with no batch id",infoCamereBatchRequests.size());
            if(infoCamereBatchRequests != null && !infoCamereBatchRequests.isEmpty()){
                hasNext = true;
                setNewBatchId(infoCamereBatchRequests);
            }
        }
        log.trace("batchPecListRequest end");
    }

    private void setNewBatchId(List<InfoCamereBatchRequest> infoCamereBatchRequests){
        String batchId = UUID.randomUUID().toString();
        List<InfoCamereBatchRequest> infoCamereBatchRequestsWithBatchId = infoCamereBatchRequestConnector.setBatchIdAndStatusWorking(infoCamereBatchRequests,batchId);
        log.info("Setted batchId: {} at Batch Requests size: {}",batchId,infoCamereBatchRequestsWithBatchId.size());
        List<String> requestCfIniPec = infoCamereBatchRequestsWithBatchId.stream()
                .filter(iniPecBatchRequest -> iniPecBatchRequest.getRetry() <= 3)
                .map(InfoCamereBatchRequest::getCf)
                .collect(Collectors.toList());
        callService(requestCfIniPec,batchId);
    }

    private void callService(List<String> requestCfIniPec, String batchId){
        InfoCamereCfRequest infoCamereCfRequest = new InfoCamereCfRequest();
        infoCamereCfRequest.setDataOraRichiesta(LocalDateTime.now().toString());
        infoCamereCfRequest.setElencoCf(requestCfIniPec);
        log.info("Calling ini pec with cf size: {} and batchId: {}",requestCfIniPec.size(),batchId);
        InfoCamerePolling infoCamerePolling = infoCamereConnector.callEServiceRequestId(infoCamereCfRequest);
        log.info("Called ini pec with batchId: {} and pollingId: {}",batchId,infoCamerePolling.getIdentificativoRichiesta());
        infoCamereBatchPollingConnector.save(createIniPecBatchPolling(batchId, infoCamerePolling.getIdentificativoRichiesta()));
        log.info("Created Batch Polling with batchId: {} and pollingId: {}",batchId,infoCamerePolling.getIdentificativoRichiesta());
    }

    private InfoCamereBatchPolling createIniPecBatchPolling(String batchId, String pollingId){
        InfoCamereBatchPolling infoCamereBatchPolling = new InfoCamereBatchPolling();
        infoCamereBatchPolling.setPollingId(pollingId);
        infoCamereBatchPolling.setBatchId(batchId);
        infoCamereBatchPolling.setStatus(BatchStatus.NOT_WORKED.getValue());
        infoCamereBatchPolling.setTimeStamp(LocalDateTime.now());
        return infoCamereBatchPolling;
    }

    public void recovery(){
        log.trace("recovery start");
        boolean hasNext = true;
        while(hasNext) {
            hasNext = false;
            List<InfoCamereBatchRequest> infoCamereBatchRequests = infoCamereBatchRequestConnector.findAllByBatchIdNotAndStatusWorking(BatchStatus.NO_BATCH_ID.getValue());
            log.info("Resetting batchId and status at Batch Requests size: {} with status at WORKING",infoCamereBatchRequests.size());
            if(infoCamereBatchRequests !=null && !infoCamereBatchRequests.isEmpty()){
                hasNext = true;
                infoCamereBatchRequests.forEach(iniPecBatchRequest -> {
                    iniPecBatchRequest.setBatchId(BatchStatus.NO_BATCH_ID.getValue());
                    iniPecBatchRequest.setStatus(BatchStatus.NOT_WORKED.getValue());
                    infoCamereBatchRequestConnector.save(iniPecBatchRequest);
                });
            }
            log.info("Resetted batchId and status at Batch Requests size: {} with status at WORKING",infoCamereBatchRequests.size());
        }
        batchPecListRequest();
        log.trace("recovery end");
    }
}
