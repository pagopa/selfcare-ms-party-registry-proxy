package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GetBusinessesByLegal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class InfoCamereServiceImpl implements InfoCamereService {
    private final InfoCamereConnector infoCamereConnector;

    @Autowired
    InfoCamereServiceImpl(InfoCamereConnector infoCamereConnector) {
        log.trace("Initializing {}", InfoCamereServiceImpl.class.getSimpleName());
        this.infoCamereConnector = infoCamereConnector;
    }
    @Override
    public Businesses businessesByLegal(GetBusinessesByLegal getBusinessesByLegalDto) {
        log.trace("businessesByLegal start");
        return this.infoCamereConnector.businessesByLegal(getBusinessesByLegalDto);
    }
}
