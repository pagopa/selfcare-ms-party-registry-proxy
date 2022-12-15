package it.pagopa.selfcare.party.registry_proxy.connector.dao;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import it.pagopa.selfcare.party.registry_proxy.connector.api.EventHubConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.MessageEventHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventHubConnectorImpl implements EventHubConnector {

    private final String connectionString;
    private final String eventHubName;

    public EventHubConnectorImpl(
            @Value("${rest-client.info-camere.event-hub.connectionString}") String connectionString,
            @Value("${rest-client.info-camere.event-hub.eventHubName}") String eventHubName) {
        this.connectionString = connectionString;
        this.eventHubName = eventHubName;
    }

    @Override
    public boolean push(MessageEventHub messageEventHub){
        EventData eventData = new EventData(String.valueOf(messageEventHub));
        EventHubProducerClient producer = new EventHubClientBuilder()
                .connectionString(connectionString, eventHubName)
                .buildProducerClient();
        EventDataBatch eventDataBatch = producer.createBatch();
        if (!eventDataBatch.tryAdd(eventData)) {
            producer.send(eventDataBatch);
            producer.close();
            return true;
        }
        else{
            producer.close();
            return false;
        }
    }
}
