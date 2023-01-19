package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereBatchRequestConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.BatchStatus;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereLegalAddress;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.MaskDataUtils;
import lombok.extern.slf4j.Slf4j;
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
    public Businesses institutionsByLegalTaxId(String legalTaxId) {
        log.info("institutionsByLegalTaxId for legalTaxId: {}", MaskDataUtils.maskString(legalTaxId));
        return this.infoCamereConnector.institutionsByLegalTaxId(legalTaxId);
    }

    @Override
    public InfoCamereBatchRequest createBatchRequestByCf(String cf){
        log.trace("createBatchReqeustByCf start");
        InfoCamereBatchRequest infoCamereBatchRequest = new InfoCamereBatchRequest();
        infoCamereBatchRequest.setCf(cf);
        infoCamereBatchRequest.setRetry(0);
        infoCamereBatchRequest.setBatchId(BatchStatus.NO_BATCH_ID.getValue());
        infoCamereBatchRequest.setStatus(BatchStatus.NOT_WORKED.getValue());
        infoCamereBatchRequest.setLastReserved(LocalDateTime.now());
        infoCamereBatchRequest.setTimeStamp(LocalDateTime.now());
        log.info("Created Batch Request for taxId: {}", MaskDataUtils.maskString(cf));
        return infoCamereBatchRequestConnector.save(infoCamereBatchRequest);
    }

    @Override
    public InfoCamereLegalAddress legalAddressByTaxId(String taxId) {
        log.info("legalAddressByTaxId for taxId: {}", MaskDataUtils.maskString(taxId));
        return this.infoCamereConnector.legalAddressByTaxId(taxId);
    }
}
