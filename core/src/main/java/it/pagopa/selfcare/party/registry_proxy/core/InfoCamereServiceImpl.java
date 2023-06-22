package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.NationalRegistriesConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.MaskDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;

@Slf4j
@Service
class InfoCamereServiceImpl implements InfoCamereService {
    private final NationalRegistriesConnector nationalRegistriesConnector;

    public InfoCamereServiceImpl(NationalRegistriesConnector nationalRegistriesConnector) {
        log.trace("Initializing {}", InfoCamereServiceImpl.class.getSimpleName());
        this.nationalRegistriesConnector = nationalRegistriesConnector;
    }
    @Override
    public Businesses institutionsByLegalTaxId(String legalTaxId) {
        log.info("institutionsByLegalTaxId for legalTaxId: {}", MaskDataUtils.maskString(legalTaxId));
        Businesses response = this.nationalRegistriesConnector.institutionsByLegalTaxId(legalTaxId);
        if(checkIfResponseIsInfoCamereNotFoundError(response)) {
            log.info("institutions not found for legalTaxId: {}",MaskDataUtils.maskString(legalTaxId));
            return createOkResponse(legalTaxId);
        }

        return response;
    }

    private boolean checkIfResponseIsInfoCamereNotFoundError(Businesses response) {
        return (
                response.getCode() != null && !"".equals(response.getCode())
                || response.getDescription() != null && !"".equals(response.getDescription())
                || response.getTimestamp() != null && !"".equals(response.getTimestamp())
                || response.getAppName() != null && !"".equals(response.getAppName())
        );
    }

    private Businesses createOkResponse(String legalTaxId) {
        Businesses newResponse = new Businesses();
        newResponse.setLegalTaxId(legalTaxId);
        newResponse.setBusinessList(new ArrayList<>());
        newResponse.setDateTimeExtraction(OffsetDateTime.now().toString());
        return newResponse;
    }

}
