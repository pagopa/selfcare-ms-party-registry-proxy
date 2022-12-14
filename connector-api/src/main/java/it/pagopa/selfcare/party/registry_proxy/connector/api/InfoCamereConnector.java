package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GetBusinessesByLegal;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereCfRequest;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamerePec;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamerePolling;

public interface InfoCamereConnector {
    Businesses businessesByLegal(GetBusinessesByLegal getBusinessesByLegal);
    InfoCamerePolling callEServiceRequestId(InfoCamereCfRequest infoCamereCfRequest);
    InfoCamerePec callEServiceRequestPec(String correlationId);
}
