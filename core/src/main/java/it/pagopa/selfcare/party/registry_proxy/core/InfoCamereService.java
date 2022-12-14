package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GetBusinessesByLegal;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchRequest;

public interface InfoCamereService {
    Businesses businessesByLegal(GetBusinessesByLegal getBusinessesByLegalDto);
    InfoCamereBatchRequest createBatchRequestByCf(String cf);


}
