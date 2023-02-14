package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.NationalRegistriesProfessionalResponse;

public interface NationalRegistriesConnector{

    NationalRegistriesProfessionalResponse getLegalAddress(String taxCode);
}
