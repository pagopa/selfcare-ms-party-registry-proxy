package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.MockOpenDataRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.MockOpenDataCategory;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.MockOpenDataInstitution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Order(2)
@ConditionalOnBean(MockOpenDataRestClient.class)
class MockOpenDataConnector extends OpenDataConnectorTemplate {

    @Autowired
    public MockOpenDataConnector(MockOpenDataRestClient restClient) {
        super(restClient);
    }


    @Override
    protected Class<MockOpenDataInstitution> getInstitutionType() {
        return MockOpenDataInstitution.class;
    }

    @Override
    protected Class<MockOpenDataCategory> getCategoryType() {
        return MockOpenDataCategory.class;
    }
}
