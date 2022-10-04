package it.pagopa.selfcare.party.registry_proxy.connector.rest.config;

import it.pagopa.selfcare.commons.connector.rest.config.RestClientBaseConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.MockOpenDataRestClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

//@Configuration
@Import(RestClientBaseConfig.class)
@PropertySource("classpath:config/mock-open-data-rest-client.properties")
//@Conditional(OpenDataMockEnabledCondition.class)
@PropertySource("classpath:config/open-data.properties")
@EnableFeignClients(clients = MockOpenDataRestClient.class)
class MockOpenDataRestClientConfig {
    public MockOpenDataRestClientConfig() {
        System.out.println();
    }
}
