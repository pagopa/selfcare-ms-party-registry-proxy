package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.NationalRegistriesConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.NationalRegistriesProfessionalResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.MaskDataUtils;
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
    public NationalRegistriesProfessionalResponse getLegalAddress(String taxId) {
        log.info("getLegalAddress for TaxId: {}", MaskDataUtils.maskString(taxId));
        return nationalRegistriesConnector.getLegalAddress(taxId);
    }

}
