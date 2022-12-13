package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GetBusinessesByLegal;

public interface InfoCamereConnector {
    Businesses businessesByLegal(GetBusinessesByLegal getBusinessesByLegal);
}
