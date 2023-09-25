package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.api.FileStorageConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.IPAOpenDataRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Order(1)
class IPAOpenDataConnector extends OpenDataConnectorTemplate {

    public IPAOpenDataConnector(IPAOpenDataRestClient restClient, FileStorageConnector fileStorageConnector) {
        super(restClient, fileStorageConnector);
        log.trace("Initializing {}", IPAOpenDataConnector.class.getSimpleName());
    }

    @Override
    protected Class<IPAOpenDataInstitution> getInstitutionType() {
        return IPAOpenDataInstitution.class;
    }

    @Override
    protected Class<IPAOpenDataCategory> getCategoryType() {
        return IPAOpenDataCategory.class;
    }

    @Override
    protected Class<IPAOpenDataAOO> getAOOType() {
        return IPAOpenDataAOO.class;
    }

    @Override
    protected Class<IPAOpenDataUO> getUOType() {
        return IPAOpenDataUO.class;
    }

    @Override
    protected Class<OpenDataPDNDTemplate> getPDNDType(){
        return OpenDataPDNDTemplate.class;
    }

}
