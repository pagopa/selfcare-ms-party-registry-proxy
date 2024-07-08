package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import it.pagopa.selfcare.commons.base.logging.LogUtils;
import it.pagopa.selfcare.party.registry_proxy.connector.api.PDNDNationalRegistriesConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDNationalRegistriesRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.*;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.mapper.PDNDBusinessMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
@Service
public class PDNDNationalRegistriesConnectorImpl implements PDNDNationalRegistriesConnector {
    public static final String ERROR_PDND_NATIONAL_REGISTRIES_REST_CLIENT_MESSAGE = "Error pdnd-national-registries rest client, message: %s";

    private final PDNDNationalRegistriesRestClient pdndNationalRegistriesRestClient;
    private final PDNDBusinessMapper pdndBusinessMapper;

    public PDNDNationalRegistriesConnectorImpl(PDNDNationalRegistriesRestClient pdndNationalRegistriesRestClient, PDNDBusinessMapper pdndBusinessMapper) {
        this.pdndNationalRegistriesRestClient = pdndNationalRegistriesRestClient;
        this.pdndBusinessMapper = pdndBusinessMapper;
    }

    @Override
    @CircuitBreaker(name = "pdndNationalRegistriesCircuitbreaker", fallbackMethod = "fallbackRetrieveInstitutionByDescription")
    @Retry(name = "retryServiceUnavailable")
    public List<PDNDBusiness> retrieveInstitutionsPdndByDescription(String description) {
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "retrieveInstitutionsPdndByDescription description = {}", description);
        Assert.hasText(description, "Description is required");

        List<PDNDImpresa> result = pdndNationalRegistriesRestClient.retrieveInstitutionsPdndByDescription(description);

        log.debug(LogUtils.CONFIDENTIAL_MARKER, "retrieveInstitutionsPdndByDescription result = {}", result);
        return pdndBusinessMapper.toPDNDBusiness(result);
    }

    public List<PDNDBusiness> fallbackRetrieveInstitutionByDescription(RuntimeException e) {
        log.error(String.format(ERROR_PDND_NATIONAL_REGISTRIES_REST_CLIENT_MESSAGE, e.getMessage()));
        return List.of();
    }

}
