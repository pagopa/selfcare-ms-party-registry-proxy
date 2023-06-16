package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.api.NationalRegistriesConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.*;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.NationalRegistriesRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NationalRegistriesConnectorImpl implements NationalRegistriesConnector {

    private final NationalRegistriesRestClient nationalRegistriesRestClient;

    public NationalRegistriesConnectorImpl(NationalRegistriesRestClient nationalRegistriesRestClient) {
        this.nationalRegistriesRestClient = nationalRegistriesRestClient;
    }

    @Override
    public LegalAddressResponse getLegalAddress(String taxCode) {
        LegalAddressRequest legalAddressRequest = createLegalAddressRequest(taxCode);
        return toLegalAddressResponse(nationalRegistriesRestClient.getLegalAddress(legalAddressRequest));
    }

    @Override
    public VerifyLegalResponse verifyLegal(String taxId, String vatNumber) {
        AdELegalRequestBodyDto request = createRequest(taxId, vatNumber);
        return toVerifyLegalResponse(nationalRegistriesRestClient.verifyLegal(request));
    }

    @Override
    public Businesses institutionsByLegalTaxId(String legalTaxId) {
        log.info("start institutionsByLegalTaxId");
        LegalInstitutionsRequestBody request = createLegalInstitutionsRequest(legalTaxId);
        return nationalRegistriesRestClient.getLegalInstitutions(request);
    }


    private LegalAddressResponse toLegalAddressResponse(GetAddressRegistroImpreseOKDto legalAddress) {
        LegalAddressResponse legalAddressResponse = new LegalAddressResponse();
        legalAddressResponse.setTaxId(legalAddress.getTaxId());
        legalAddressResponse.setProfessionalAddress(toProfessionalAddress(legalAddress.getProfessionalAddress()));
        legalAddressResponse.setDateTimeExtraction(legalAddress.getDateTimeExtraction());
        return legalAddressResponse;
    }

    private LegalAddressProfessionalResponse toProfessionalAddress(ProfessionalAddressDto professionalAddress) {
        LegalAddressProfessionalResponse legalAddressProfessionalResponse = new LegalAddressProfessionalResponse();
        legalAddressProfessionalResponse.setAddress(professionalAddress.getAddress());
        legalAddressProfessionalResponse.setZip(professionalAddress.getZip());
        legalAddressProfessionalResponse.setProvince(professionalAddress.getProvince());
        legalAddressProfessionalResponse.setMunicipality(professionalAddress.getMunicipality());
        legalAddressProfessionalResponse.setDescription(professionalAddress.getDescription());
        return legalAddressProfessionalResponse;
    }

    private VerifyLegalResponse toVerifyLegalResponse(AdELegalOKDto adELegalOKDto) {
        VerifyLegalResponse verifyLegalResponse = new VerifyLegalResponse();
        verifyLegalResponse.setVerificationResult(adELegalOKDto.getVerificationResult());
        verifyLegalResponse.setVerifyLegalResultDetail(adELegalOKDto.getResultDetail().getValue());
        verifyLegalResponse.setVerifyLegalResultCode(adELegalOKDto.getResultCode().getValue());
        return verifyLegalResponse;
    }

    private LegalAddressRequest createLegalAddressRequest(String taxCode) {
        LegalAddressRequest legalAddressRequest = new LegalAddressRequest();
        LegalAddressFilter filter = new LegalAddressFilter();
        filter.setTaxId(taxCode);
        legalAddressRequest.setFilter(filter);
        return legalAddressRequest;
    }

    private LegalInstitutionsRequestBody createLegalInstitutionsRequest(String taxCode) {
        LegalInstitutionsRequestBody legalInstitutionsRequest = new LegalInstitutionsRequestBody();
        LegalInstutionsRequestBodyFilterDto filter = new LegalInstutionsRequestBodyFilterDto();
        filter.setTaxId(taxCode);
        legalInstitutionsRequest.setFilter(filter);
        return legalInstitutionsRequest;
    }

    private AdELegalRequestBodyDto createRequest(String taxCode, String vatNumber) {
        AdELegalRequestBodyDto requestBodyDto = new AdELegalRequestBodyDto();
        AdELegalRequestBodyFilterDto filter = new AdELegalRequestBodyFilterDto();
        filter.setTaxId(taxCode);
        filter.setVatNumber(vatNumber);
        requestBodyDto.setFilter(filter);
        return requestBodyDto;
    }
}
