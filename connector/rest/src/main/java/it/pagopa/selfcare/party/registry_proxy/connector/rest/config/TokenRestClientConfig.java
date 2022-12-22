package it.pagopa.selfcare.party.registry_proxy.connector.rest.config;

import it.pagopa.selfcare.commons.connector.rest.config.RestClientBaseConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.InfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.TokenRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@Configuration
@Import(RestClientBaseConfig.class)
@EnableFeignClients(clients = TokenRestClient.class)
@PropertySource("classpath:config/info-camere-rest-client.properties")
public class TokenRestClientConfig {
    public TokenRestClientConfig() {
        log.trace("Initializing {}", TokenRestClientConfig.class.getSimpleName());
    }
}
