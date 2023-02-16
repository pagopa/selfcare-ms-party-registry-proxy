package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.MaskDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class InfoCamereServiceImpl implements InfoCamereService {
    private final InfoCamereConnector infoCamereConnector;

    public InfoCamereServiceImpl(InfoCamereConnector infoCamereConnector) {
        log.trace("Initializing {}", InfoCamereServiceImpl.class.getSimpleName());
        this.infoCamereConnector = infoCamereConnector;
    }
    @Override
    public Businesses institutionsByLegalTaxId(String legalTaxId) {
        log.info("institutionsByLegalTaxId for legalTaxId: {}", MaskDataUtils.maskString(legalTaxId));
        return this.infoCamereConnector.institutionsByLegalTaxId(legalTaxId);
    }

}
