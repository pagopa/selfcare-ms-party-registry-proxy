package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.NationalRegistriesProfessionalResponse;

public interface NationalRegistriesService {

    NationalRegistriesProfessionalResponse getLegalAddress(String taxId);

}
