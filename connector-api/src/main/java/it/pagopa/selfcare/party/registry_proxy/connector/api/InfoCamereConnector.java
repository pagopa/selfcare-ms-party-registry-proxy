package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereCfRequest;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamerePec;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamerePolling;

public interface InfoCamereConnector {
    Businesses businessesByLegalTaxId(String legalTaxId);
    InfoCamerePolling callEServiceRequestId(InfoCamereCfRequest infoCamereCfRequest);
    InfoCamerePec callEServiceRequestPec(String correlationId);
}
