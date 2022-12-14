package it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InfoCamereBatchPolling {

    private String id;
    private String batchId;
    private String pollingId;
    private String status;
    private LocalDateTime timeStamp;

}
