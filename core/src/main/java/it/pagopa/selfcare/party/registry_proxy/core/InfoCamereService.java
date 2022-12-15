package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchRequest;

public interface InfoCamereService {
    Businesses businessesByLegal(String legalTaxId);
    InfoCamereBatchRequest createBatchRequestByCf(String cf);


}
