package it.pagopa.selfcare.party.registry_proxy.connector.rest.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(InfoCamereRestClientConfig.class)
public class InfoCamereRestClientTestConfig {
}
