package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.Businesses;

public interface InfoCamereService {
    Businesses institutionsByLegalTaxId(String legalTaxId);

}
