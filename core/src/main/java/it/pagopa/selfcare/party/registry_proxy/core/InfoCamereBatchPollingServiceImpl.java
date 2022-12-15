package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.EventHubConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereBatchPollingConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereBatchRequestConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InfoCamereBatchPollingServiceImpl implements InfoCamereBatchPollingService {

    private final InfoCamereBatchRequestConnector infoCamereBatchRequestConnector;
    private final InfoCamereBatchPollingConnector infoCamereBatchPollingConnector;
    private final InfoCamereConnector infoCamereConnector;
    private final EventHubConnector eventHubConnector;

    public InfoCamereBatchPollingServiceImpl(
            InfoCamereBatchRequestConnector infoCamereBatchRequestConnector,
            InfoCamereBatchPollingConnector infoCamereBatchPollingConnector,
            InfoCamereConnector infoCamereConnector,
            EventHubConnector eventHubConnector) {
        this.infoCamereBatchRequestConnector = infoCamereBatchRequestConnector;
        this.infoCamereBatchPollingConnector = infoCamereBatchPollingConnector;
        this.infoCamereConnector = infoCamereConnector;
        this.eventHubConnector = eventHubConnector;
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
        sendMessageEventHub(infoCamereBatchPolling.getBatchId(),infoCamerePec);
        setWorkedBatchPolling(infoCamereBatchPolling);
    }

    private void sendMessageEventHub(String batchId, InfoCamerePec response){
        List<InfoCamereBatchRequest> infoCamereBatchRequests = infoCamereBatchRequestConnector.findAllByBatchId(batchId);
        for(Pec pec : response.getElencoPec()){
            Optional<InfoCamereBatchRequest> opt = infoCamereBatchRequests.stream().filter(infoCamereBatchRequest -> infoCamereBatchRequest.getCf().equalsIgnoreCase(pec.getCf())).findAny();
            if(opt.isPresent()){
                MessageEventHub messageEventHub = createMessageEventHub(opt.get(),pec);
                sendMessageEventHub(messageEventHub,opt.get());
            }
        }
    }

    private MessageEventHub createMessageEventHub(InfoCamereBatchRequest infoCamereBatchRequest, Pec pec){
        MessageEventHub messageEventHub = new MessageEventHub();
        messageEventHub.setCorrelationId(infoCamereBatchRequest.getId());
        messageEventHub.setTaxId(pec.getCf());
        messageEventHub.setPrimaryDigitalAddress(new DigitalAddress(DigitalAddressType.PEC.getValue(),pec.getPecImpresa()));
        messageEventHub.setSecondaryDigitalAddresses(pec.getPecProfessionistas().stream().map(s -> new DigitalAddress(DigitalAddressType.PEC.getValue(),s)).collect(Collectors.toCollection(ArrayList::new)));
        messageEventHub.setPrimaryPhysicalAddress(null);
        messageEventHub.setSecondaryPhysicalAddresses(null);
        return messageEventHub;
    }
    private void sendMessageEventHub(MessageEventHub messageEventHub, InfoCamereBatchRequest infoCamereBatchRequest){
        if(eventHubConnector.push(messageEventHub)){
            setWorkedBatchRequest(infoCamereBatchRequest);
        }
    }

    private void setWorkedBatchPolling(InfoCamereBatchPolling infoCamereBatchPolling){
        infoCamereBatchPolling.setStatus(BatchStatus.WORKED.getValue());
        infoCamereBatchPollingConnector.save(infoCamereBatchPolling);
    }

    private void setWorkedBatchRequest(InfoCamereBatchRequest infoCamereBatchRequest){
        infoCamereBatchRequest.setStatus(BatchStatus.WORKED.getValue());
        infoCamereBatchRequestConnector.save(infoCamereBatchRequest);
    }
}
