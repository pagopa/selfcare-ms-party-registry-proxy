package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import it.pagopa.selfcare.party.registry_proxy.connector.api.PDNDInfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDInfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.*;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.mapper.PDNDBusinessMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
@Service
public class PDNDInfoCamereConnectorImpl implements PDNDInfoCamereConnector {
    public static final String ERROR_PDND_INFO_CAMERE_REST_CLIENT_MESSAGE = "Error pdnd-infocamere rest client, message: %s";

    private final PDNDInfoCamereRestClient pdndInfoCamereRestClient;
    private final PDNDBusinessMapper pdndBusinessMapper;

    public PDNDInfoCamereConnectorImpl(PDNDInfoCamereRestClient pdndInfoCamereRestClient, PDNDBusinessMapper pdndBusinessMapper) {
        this.pdndInfoCamereRestClient = pdndInfoCamereRestClient;
        this.pdndBusinessMapper = pdndBusinessMapper;
    }

    @Override
    @CircuitBreaker(name = "pdndInfoCamereCircuitbreaker", fallbackMethod = "fallbackRetrieveInstitutionByDescription")
    @Retry(name = "retryServiceUnavailable")
    public List<PDNDBusiness> retrieveInstitutionsPdndByDescription(String description) {
        Assert.hasText(description, "Description is required");
        List<PDNDImpresa> result = pdndInfoCamereRestClient.retrieveInstitutionsPdndByDescription(description);
        return pdndBusinessMapper.toPDNDBusinesses(result);
    }

    public List<PDNDBusiness> fallbackRetrieveInstitutionByDescription(RuntimeException e) {
        log.error(String.format(ERROR_PDND_INFO_CAMERE_REST_CLIENT_MESSAGE, e.getMessage()));
        return List.of();
    }

    @Override
    @CircuitBreaker(name = "pdndInfoCamereCircuitbreaker", fallbackMethod = "fallbackRetrieveInstitutionByTaxCode")
    @Retry(name = "retryServiceUnavailable")
    public PDNDBusiness retrieveInstitutionPdndByTaxCode(String taxCode) {
        Assert.hasText(taxCode, "TaxCode is required");
        PDNDImpresa result = pdndInfoCamereRestClient.retrieveInstitutionPdndByTaxCode(taxCode).get(0);
        return pdndBusinessMapper.toPDNDBusiness(result);
    }

    public PDNDBusiness fallbackRetrieveInstitutionByTaxCode(RuntimeException e) {
        log.error(String.format(ERROR_PDND_INFO_CAMERE_REST_CLIENT_MESSAGE, e.getMessage()));
        throw new ResourceNotFoundException("");
    }

}
