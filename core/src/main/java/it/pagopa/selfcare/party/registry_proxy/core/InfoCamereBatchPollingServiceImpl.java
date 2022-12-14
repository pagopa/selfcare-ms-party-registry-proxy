package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereBatchPollingConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereBatchRequestConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.BatchStatus;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchPolling;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamerePec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InfoCamereBatchPollingServiceImpl implements InfoCamereBatchPollingService {

    private final InfoCamereBatchRequestConnector infoCamereBatchRequestConnector;
    private final InfoCamereBatchPollingConnector infoCamereBatchPollingConnector;
    private final InfoCamereConnector infoCamereConnector;

    public InfoCamereBatchPollingServiceImpl(
            InfoCamereBatchRequestConnector infoCamereBatchRequestConnector,
            InfoCamereBatchPollingConnector infoCamereBatchPollingConnector,
            InfoCamereConnector infoCamereConnector) {
        this.infoCamereBatchRequestConnector = infoCamereBatchRequestConnector;
        this.infoCamereBatchPollingConnector = infoCamereBatchPollingConnector;
        this.infoCamereConnector = infoCamereConnector;
    }

    @Override
    public void getPecList() {
        boolean hasNext = true;

        while(hasNext){
            hasNext = false;
            List<InfoCamereBatchPolling> infoCamereBatchPollings = infoCamereBatchPollingConnector.findByStatus(BatchStatus.NOT_WORKED.getValue());
            if(infoCamereBatchPollings !=null && !infoCamereBatchPollings.isEmpty()){
                hasNext = true;
                InfoCamereBatchPolling infoCamereBatchPolling = infoCamereBatchPollings.get(0);
                callService(infoCamereBatchPolling);
            }
        }
    }

    private void callService(InfoCamereBatchPolling infoCamereBatchPolling){
        InfoCamerePec infoCamerePec = infoCamereConnector.callEServiceRequestPec(infoCamereBatchPolling.getPollingId());
        log.info("Calling ini pec with pec size: {} and pollingId: {}",infoCamerePec.getElencoPec().size(),infoCamerePec.getIdentificativoRichiesta());
        //Invio email per firma contratto
        setWorkedBatchPolling(infoCamereBatchPolling);
    }

    private void setWorkedBatchPolling(InfoCamereBatchPolling infoCamereBatchPolling){
        infoCamereBatchPolling.setStatus(BatchStatus.WORKED.getValue());
        infoCamereBatchPollingConnector.save(infoCamereBatchPolling);
        setWorkedBatchRequestByBatchId(infoCamereBatchPolling.getBatchId());
    }

    private void setWorkedBatchRequestByBatchId(String batchId){
        List<InfoCamereBatchRequest> infoCamereBatchRequests = infoCamereBatchRequestConnector.findAllByBatchId(batchId);
        infoCamereBatchRequests.forEach(iniPecBatchRequest -> {
            iniPecBatchRequest.setStatus(BatchStatus.WORKED.getValue());
            infoCamereBatchRequestConnector.save(iniPecBatchRequest);
        });
    }
}
