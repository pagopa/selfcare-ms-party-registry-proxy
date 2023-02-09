package it.pagopa.selfcare.party.registry_proxy.connector.rest.config;

import it.pagopa.selfcare.commons.connector.rest.config.RestClientBaseConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.InfoCamereRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@Configuration
@Import(RestClientBaseConfig.class)
@EnableFeignClients(clients = InfoCamereRestClient.class)
@PropertySource("classpath:config/info-camere-rest-client.properties")
public class InfoCamereRestClientConfig {
    public InfoCamereRestClientConfig() {
        log.trace("Initializing {}", InfoCamereRestClientConfig.class.getSimpleName());
    }
}
