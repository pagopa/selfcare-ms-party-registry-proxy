package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.NationalRegistriesProfessionalResponse;
import it.pagopa.selfcare.party.registry_proxy.web.model.LegalAddressResponse;

public class NationalRegistriesMapper {

    public static LegalAddressResponse toResource(NationalRegistriesProfessionalResponse response) {
        LegalAddressResponse legalAddressResponse = new LegalAddressResponse();
        legalAddressResponse.setAddress(response.getAddress());
        legalAddressResponse.setZipCode(response.getZip());
        return legalAddressResponse;
    }
}
