package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.LegalAddressResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.VerifyLegalResponse;

public interface NationalRegistriesConnector{

    LegalAddressResponse getLegalAddress(String taxCode);

    VerifyLegalResponse verifyLegal(String taxId, String vatNumber);
}
