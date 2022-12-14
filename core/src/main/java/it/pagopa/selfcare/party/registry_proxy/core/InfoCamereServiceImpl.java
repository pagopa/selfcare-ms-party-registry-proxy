package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereBatchRequestConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GetBusinessesByLegal;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.BatchStatus;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
class InfoCamereServiceImpl implements InfoCamereService {
    private final InfoCamereConnector infoCamereConnector;
    private final InfoCamereBatchRequestConnector infoCamereBatchRequestConnector;

    public InfoCamereServiceImpl(InfoCamereConnector infoCamereConnector,
                          InfoCamereBatchRequestConnector infoCamereBatchRequestConnector) {
        log.trace("Initializing {}", InfoCamereServiceImpl.class.getSimpleName());
        this.infoCamereConnector = infoCamereConnector;
        this.infoCamereBatchRequestConnector = infoCamereBatchRequestConnector;
    }
    @Override
    public Businesses businessesByLegal(GetBusinessesByLegal getBusinessesByLegalDto) {
        log.trace("businessesByLegal start");
        return this.infoCamereConnector.businessesByLegal(getBusinessesByLegalDto);
    }

    @Override
    public InfoCamereBatchRequest createBatchRequestByCf(String cf){
        InfoCamereBatchRequest infoCamereBatchRequest = new InfoCamereBatchRequest();
        infoCamereBatchRequest.setCf(cf);
        infoCamereBatchRequest.setRetry(0);
        infoCamereBatchRequest.setBatchId(BatchStatus.NO_BATCH_ID.getValue());
        infoCamereBatchRequest.setStatus(BatchStatus.NOT_WORKED.getValue());
        infoCamereBatchRequest.setLastReserved(LocalDateTime.now());
        infoCamereBatchRequest.setTimeStamp(LocalDateTime.now());
        log.info("Created Batch Request for taxId: {}",cf);
        return infoCamereBatchRequestConnector.save(infoCamereBatchRequest);
    }
}
