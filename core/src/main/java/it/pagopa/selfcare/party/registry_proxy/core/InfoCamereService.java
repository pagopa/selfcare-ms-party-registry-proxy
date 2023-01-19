package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereLegalAddress;

public interface InfoCamereService {
    Businesses institutionsByLegalTaxId(String legalTaxId);
    InfoCamereBatchRequest createBatchRequestByCf(String cf);

    InfoCamereLegalAddress legalAddressByTaxId(String taxId);

}
