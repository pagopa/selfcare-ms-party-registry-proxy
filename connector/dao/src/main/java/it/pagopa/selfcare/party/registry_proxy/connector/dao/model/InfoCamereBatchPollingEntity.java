package it.pagopa.selfcare.party.registry_proxy.connector.dao.model;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchPolling;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document("InfoCamereBatchPolling")
public class InfoCamereBatchPollingEntity {

    @MongoId
    private ObjectId id;
    private String batchId;
    private String pollingId;
    private String status;
    private LocalDateTime timeStamp;

    public InfoCamereBatchPollingEntity(InfoCamereBatchPolling infoCamereBatchPolling) {
        if (infoCamereBatchPolling.getId() != null) {
            id = new ObjectId(infoCamereBatchPolling.getId());
        }
        batchId = infoCamereBatchPolling.getBatchId();
        pollingId = infoCamereBatchPolling.getPollingId();
        status = infoCamereBatchPolling.getStatus();
        timeStamp = infoCamereBatchPolling.getTimeStamp();
    }
}
