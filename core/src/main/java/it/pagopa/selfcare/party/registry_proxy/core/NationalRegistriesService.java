package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.LegalAddressProfessionalResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.VerifyLegalResponse;

public interface NationalRegistriesService {

    LegalAddressProfessionalResponse getLegalAddress(String taxId);

    VerifyLegalResponse verifyLegal(String taxId, String vatNumber);
}
