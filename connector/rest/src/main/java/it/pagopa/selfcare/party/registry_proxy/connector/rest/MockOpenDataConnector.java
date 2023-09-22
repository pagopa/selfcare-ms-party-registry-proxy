package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.api.FileStorageConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.MockOpenDataRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.OpenDataMockEnabledCondition;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Slf4j
@Conditional(OpenDataMockEnabledCondition.class)
@Service
@Order(2)
class MockOpenDataConnector extends OpenDataConnectorTemplate {

    @Autowired
    public MockOpenDataConnector(MockOpenDataRestClient restClient, FileStorageConnector fileStorageConnector) {
        super(restClient, fileStorageConnector);
        log.trace("Initializing {}", MockOpenDataConnector.class.getSimpleName());
    }


    @Override
    protected Class<MockOpenDataInstitution> getInstitutionType() {
        return MockOpenDataInstitution.class;
    }

    @Override
    protected Class<MockOpenDataCategory> getCategoryType() {
        return MockOpenDataCategory.class;
    }

    @Override
    protected Class<MockOpenDataAOO> getAOOType() {
        return MockOpenDataAOO.class;
    }

    @Override
    protected Class<MockOpenDataUO> getUOType() {
        return MockOpenDataUO.class;
    }

    @Override
    protected Class<MockOpenDataPDND> getPDNDType(){
        return MockOpenDataPDND.class;
    }
}
