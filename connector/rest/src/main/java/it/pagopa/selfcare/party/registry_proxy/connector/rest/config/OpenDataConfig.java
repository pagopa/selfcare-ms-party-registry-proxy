package it.pagopa.selfcare.party.registry_proxy.connector.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/open-data.properties")
public class OpenDataConfig {
    public OpenDataConfig() {
        System.out.println();
    }
}
