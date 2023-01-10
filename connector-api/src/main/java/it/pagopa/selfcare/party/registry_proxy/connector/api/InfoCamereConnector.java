package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.*;

public interface InfoCamereConnector {
    Businesses businessesByLegalTaxId(String legalTaxId);
    InfoCamerePolling callEServiceRequestId(InfoCamereCfRequest infoCamereCfRequest);
    InfoCamerePec callEServiceRequestPec(String correlationId);
    InfoCamereLegalAddress legalAddressByTaxId(String taxId);
}
