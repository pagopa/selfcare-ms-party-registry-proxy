package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.LegalAddressProfessionalResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.VerifyLegalResponse;
import it.pagopa.selfcare.party.registry_proxy.core.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.web.model.LegalAddressResponse;
import it.pagopa.selfcare.party.registry_proxy.web.model.LegalVerificationResult;
import org.springframework.util.StringUtils;

public class NationalRegistriesMapper {

    private NationalRegistriesMapper() {
    }

    public static LegalAddressResponse toResource(LegalAddressProfessionalResponse response) {
        if(!StringUtils.hasText(response.getAddress())) {
            throw new ResourceNotFoundException("Legal Address not found");
        }
        LegalAddressResponse legalAddressResponse = new LegalAddressResponse();
        legalAddressResponse.setAddress(constructAddress(response));
        legalAddressResponse.setZipCode(response.getZip());
        return legalAddressResponse;
    }

    private static String constructAddress(LegalAddressProfessionalResponse response) {
        StringBuilder stringBuilder = new StringBuilder(response.getAddress());
        if(StringUtils.hasText(response.getMunicipality())) {
            stringBuilder.append(", ");
            stringBuilder.append(response.getMunicipality());
        }
        if(StringUtils.hasText(response.getProvince())) {
            stringBuilder.append(" (");
            stringBuilder.append(response.getProvince());
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }

    public static LegalVerificationResult toResult(VerifyLegalResponse response) {
        LegalVerificationResult legalVerificationResult = new LegalVerificationResult();
        legalVerificationResult.setResultCode(response.getVerifyLegalResultCode());
        legalVerificationResult.setResultDetail(response.getVerifyLegalResultDetail());
        legalVerificationResult.setVerificationResult(response.isVerificationResult());
        return legalVerificationResult;
    }
}
