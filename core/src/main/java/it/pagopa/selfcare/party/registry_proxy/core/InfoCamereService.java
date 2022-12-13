package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GetBusinessesByLegal;

public interface InfoCamereService {
    Businesses businessesByLegal(GetBusinessesByLegal getBusinessesByLegalDto);
}
