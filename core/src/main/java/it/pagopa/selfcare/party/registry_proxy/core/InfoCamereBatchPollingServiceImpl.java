package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.EventHubConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereBatchPollingConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereBatchRequestConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.*;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.MaskDataUtils;
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
        log.trace("getPecList start");
        boolean hasNext = true;
        while(hasNext){
            hasNext = false;
            List<InfoCamereBatchPolling> infoCamereBatchPollings = infoCamereBatchPollingConnector.findByStatus(BatchStatus.NOT_WORKED.getValue());
            log.info("Found batchPollings size: {} with not worked",infoCamereBatchPollings.size());
            if(infoCamereBatchPollings !=null && !infoCamereBatchPollings.isEmpty()){
                hasNext = true;
                InfoCamereBatchPolling infoCamereBatchPolling = infoCamereBatchPollings.get(0);
                callService(infoCamereBatchPolling);
            }
        }
        log.trace("getPecList end");
    }

    private void callService(InfoCamereBatchPolling infoCamereBatchPolling){
        log.info("Calling ini pec with batchId: {} and pollingId: {}",infoCamereBatchPolling.getBatchId(), infoCamereBatchPolling.getPollingId());
        InfoCamerePec infoCamerePec = infoCamereConnector.callEServiceRequestPec(infoCamereBatchPolling.getPollingId());
        log.info("Called ini pec with batchId: {} and pollingId: {} and pec size: {}",infoCamereBatchPolling.getBatchId(), infoCamereBatchPolling.getPollingId(),infoCamerePec.getElencoPec().size());
        sendMessageEventHub(infoCamereBatchPolling.getBatchId(),infoCamerePec);
        setWorkedBatchPolling(infoCamereBatchPolling);
    }

    private void sendMessageEventHub(String batchId, InfoCamerePec response){
        List<InfoCamereBatchRequest> infoCamereBatchRequests = infoCamereBatchRequestConnector.findAllByBatchId(batchId);
        for(Pec pec : response.getElencoPec()){
            Optional<InfoCamereBatchRequest> opt = infoCamereBatchRequests.stream().filter(infoCamereBatchRequest -> infoCamereBatchRequest.getCf().equalsIgnoreCase(pec.getCf())).findAny();
            if(opt.isPresent()){
                MessageEventHub messageEventHub = createMessageEventHub(opt.get(),pec);
                //if(!ObjectUtils.isEmpty(messageEventHub.getPrimaryDigitalAddress().getAddress()) || messageEventHub.getSecondaryDigitalAddresses().size()!=0)
                sendMessageEventHub(messageEventHub,opt.get());
            }
        }
    }

    private MessageEventHub createMessageEventHub(InfoCamereBatchRequest infoCamereBatchRequest, Pec pec){
        MessageEventHub messageEventHub = new MessageEventHub();
        messageEventHub.setCorrelationId(infoCamereBatchRequest.getId());
        messageEventHub.setTaxId(pec.getCf());
        messageEventHub.setPrimaryDigitalAddress(new DigitalAddress(DigitalAddressType.PEC.getValue(),pec.getPecImpresa()));
        messageEventHub.setSecondaryDigitalAddresses(pec.getPecProfessionistas().stream().map(s -> new DigitalAddress(DigitalAddressType.PEC.getValue(),s.getPecProfessionista())).collect(Collectors.toCollection(ArrayList::new)));
        messageEventHub.setPrimaryPhysicalAddress(null);
        messageEventHub.setSecondaryPhysicalAddresses(null);
        return messageEventHub;
    }
    private void sendMessageEventHub(MessageEventHub messageEventHub, InfoCamereBatchRequest infoCamereBatchRequest){
        log.info("Pushing message with correlationId: {} and taxId: {} to Event Hub",messageEventHub.getCorrelationId(), MaskDataUtils.maskString(messageEventHub.getTaxId()));
        if(eventHubConnector.push(messageEventHub)){
            log.info("Pushed message with correlationId: {} and taxId: {} to Event Hub",messageEventHub.getCorrelationId(), MaskDataUtils.maskString(messageEventHub.getTaxId()));
            setWorkedBatchRequest(infoCamereBatchRequest);
        }
        else{
            log.info("Failed to push message with correlationId: {} and taxId: {} to Event Hub",messageEventHub.getCorrelationId(), MaskDataUtils.maskString(messageEventHub.getTaxId()));
        }
    }

    private void setWorkedBatchPolling(InfoCamereBatchPolling infoCamereBatchPolling){
        infoCamereBatchPolling.setStatus(BatchStatus.WORKED.getValue());
        infoCamereBatchPollingConnector.save(infoCamereBatchPolling);
        log.info("Batch Polling with batch id:{} and polling id:{} set status to WORKED",infoCamereBatchPolling.getBatchId(),infoCamereBatchPolling.getPollingId());
    }

    private void setWorkedBatchRequest(InfoCamereBatchRequest infoCamereBatchRequest){
        infoCamereBatchRequest.setStatus(BatchStatus.WORKED.getValue());
        infoCamereBatchRequestConnector.save(infoCamereBatchRequest);
        log.info("Batch Request with id:{} and batch id:{} set status to WORKED",infoCamereBatchRequest.getId(),infoCamereBatchRequest.getBatchId());
    }
}
