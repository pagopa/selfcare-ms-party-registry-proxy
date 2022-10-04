package it.pagopa.selfcare.party.registry_proxy.connector.rest.config;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class OpenDataMockEnabledCondition extends AllNestedConditions {

    public OpenDataMockEnabledCondition() {
        super(ConfigurationPhase.PARSE_CONFIGURATION);
    }

    @ConditionalOnProperty(prefix = "open-data.mock", name = "enabled", havingValue = "true")
    public static class OpenDataMockEnabled {
    }

}