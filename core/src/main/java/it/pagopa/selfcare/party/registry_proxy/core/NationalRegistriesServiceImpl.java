package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.NationalRegistriesConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.LegalAddressResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.LegalAddressProfessionalResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.VerifyLegalResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.MaskDataUtils;
import it.pagopa.selfcare.party.registry_proxy.core.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class NationalRegistriesServiceImpl implements NationalRegistriesService {

    private final NationalRegistriesConnector nationalRegistriesConnector;

    NationalRegistriesServiceImpl(NationalRegistriesConnector nationalRegistriesConnector) {
        this.nationalRegistriesConnector = nationalRegistriesConnector;
    }


    @Override
    public LegalAddressProfessionalResponse getLegalAddress(String taxId) {
        log.info("getLegalAddress for TaxId: {}", MaskDataUtils.maskString(taxId));
        LegalAddressResponse response = nationalRegistriesConnector.getLegalAddress(taxId);
        if (response == null || response.getProfessionalAddress() == null) {
            throw new ResourceNotFoundException();
        }
        return toNationalRegistriesProfessionalResponse(response);
    }

    @Override
    public VerifyLegalResponse verifyLegal(String taxId, String vatNumber) {
        log.info("verify legal {} for vatNumber: {}", MaskDataUtils.maskString(taxId), MaskDataUtils.maskString(vatNumber));
        return nationalRegistriesConnector.verifyLegal(taxId, vatNumber);
    }

    private LegalAddressProfessionalResponse toNationalRegistriesProfessionalResponse(LegalAddressResponse response) {
        LegalAddressProfessionalResponse result = new LegalAddressProfessionalResponse();
        if (response.getProfessionalAddress() != null) {
            result.setAddress(response.getProfessionalAddress().getAddress());
            result.setZip(response.getProfessionalAddress().getZip());
            result.setDescription(response.getProfessionalAddress().getDescription());
            result.setMunicipality(response.getProfessionalAddress().getMunicipality());
            result.setProvince(response.getProfessionalAddress().getProvince());
        }
        return result;
    }

}
