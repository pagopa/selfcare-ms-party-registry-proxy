package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.MessageEventHub;

public interface EventHubConnector {
    boolean push(MessageEventHub messageEventHub);
}
