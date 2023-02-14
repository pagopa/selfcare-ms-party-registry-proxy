package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.NationalRegistriesProfessionalAddress;
import it.pagopa.selfcare.mscore.model.nationalregistries.NationalRegistriesAddressFilter;
import it.pagopa.selfcare.mscore.model.nationalregistries.NationalRegistriesAddressRequest;
import it.pagopa.selfcare.mscore.model.nationalregistries.NationalRegistriesAddressResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.api.NationalRegistriesConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.NationalRegistriesProfessionalResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.NationalRegistriesRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.CREATE_INSTITUTION_NOT_FOUND;

@Slf4j
@Service
public class NationalRegistriesConnectorImpl implements NationalRegistriesConnector {

    private final NationalRegistriesRestClient nationalRegistriesRestClient;

    public NationalRegistriesConnectorImpl(NationalRegistriesRestClient nationalRegistriesRestClient) {
        this.nationalRegistriesRestClient = nationalRegistriesRestClient;
    }

    @Override
    public NationalRegistriesProfessionalResponse getLegalAddress(String taxCode) {
        NationalRegistriesAddressRequest nationalRegistriesAddressRequest = createRequest(taxCode);
        NationalRegistriesAddressResponse response = nationalRegistriesRestClient.getLegalAddress(nationalRegistriesAddressRequest);
        if (response == null || response.getProfessionalAddress() == null) {
            throw new ResourceNotFoundException(String.format(CREATE_INSTITUTION_NOT_FOUND.getMessage(), taxCode), CREATE_INSTITUTION_NOT_FOUND.getCode());
        }
        return toNationalRegistriesProfessionalResponse(response);
    }

    private NationalRegistriesProfessionalResponse toNationalRegistriesProfessionalResponse(NationalRegistriesAddressResponse response) {
        NationalRegistriesProfessionalResponse result = new NationalRegistriesProfessionalResponse();
        if (response.getProfessionalAddress() != null) {
            result.setAddress(response.getProfessionalAddress().getAddress());
            result.setZip(response.getProfessionalAddress().getZip());
            result.setDescription(response.getProfessionalAddress().getDescription());
            result.setMunicipality(response.getProfessionalAddress().getMunicipality());
            result.setProvince(response.getProfessionalAddress().getProvince());
        }
        return result;
    }

    private NationalRegistriesAddressRequest createRequest(String taxCode) {
        NationalRegistriesAddressRequest nationalRegistriesAddressRequest = new NationalRegistriesAddressRequest();
        NationalRegistriesAddressFilter filter = new NationalRegistriesAddressFilter();
        filter.setTaxId(taxCode);
        nationalRegistriesAddressRequest.setFilter(filter);
        return nationalRegistriesAddressRequest;
    }
}
