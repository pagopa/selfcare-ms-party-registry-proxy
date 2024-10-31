package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import it.pagopa.selfcare.party.registry_proxy.connector.api.PDNDInfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDInfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.PDNDInfoCamereRestClientConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.ClientCredentialsResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PDNDImpresa;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.mapper.PDNDBusinessMapper;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.service.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
@Service
public class PDNDInfoCamereConnectorImpl implements PDNDInfoCamereConnector {
  public static final String ERROR_PDND_INFO_CAMERE_REST_CLIENT_MESSAGE =
      "Error pdnd-infocamere rest client, message: %s";

  private final PDNDInfoCamereRestClient pdndInfoCamereRestClient;
  private final PDNDBusinessMapper pdndBusinessMapper;
  private final TokenProvider tokenProvider;
  private final PDNDInfoCamereRestClientConfig pdndInfoCamereRestClientConfig;

  public PDNDInfoCamereConnectorImpl(
      PDNDInfoCamereRestClient pdndInfoCamereRestClient,
      PDNDBusinessMapper pdndBusinessMapper,
      TokenProvider tokenProvider,
      PDNDInfoCamereRestClientConfig pdndInfoCamereRestClientConfig) {
    this.pdndInfoCamereRestClient = pdndInfoCamereRestClient;
    this.pdndBusinessMapper = pdndBusinessMapper;
    this.tokenProvider = tokenProvider;
    this.pdndInfoCamereRestClientConfig = pdndInfoCamereRestClientConfig;
  }

  @Override
  @CircuitBreaker(
      name = "pdndInfoCamereCircuitbreaker",
      fallbackMethod = "fallbackRetrieveInstitutionByDescription")
  @Retry(name = "retryServiceUnavailable")
  public List<PDNDBusiness> retrieveInstitutionsPdndByDescription(String description) {
    Assert.hasText(description, "Description is required");
    ClientCredentialsResponse tokenResponse =
        tokenProvider.getTokenPdnd(pdndInfoCamereRestClientConfig.getPdndSecretValue());
    String bearer = "Bearer " + tokenResponse.getAccessToken();
    List<PDNDImpresa> result =
        pdndInfoCamereRestClient.retrieveInstitutionsPdndByDescription(description, bearer);
    return pdndBusinessMapper.toPDNDBusinesses(result);
  }

  public List<PDNDBusiness> fallbackRetrieveInstitutionByDescription(RuntimeException e) {
    log.error(String.format(ERROR_PDND_INFO_CAMERE_REST_CLIENT_MESSAGE, e.getMessage()));
    return List.of();
  }

  @Override
  // @CircuitBreaker(name = "pdndInfoCamereCircuitbreaker", fallbackMethod =
  // "fallbackRetrieveInstitutionByTaxCode")
  // @Retry(name = "retryServiceUnavailable")
  public PDNDBusiness retrieveInstitutionPdndByTaxCode(String taxCode) {
    Assert.hasText(taxCode, "TaxCode is required");
    ClientCredentialsResponse tokenResponse =
        tokenProvider.getTokenPdnd(pdndInfoCamereRestClientConfig.getPdndSecretValue());
    String bearer = "Bearer " + tokenResponse.getAccessToken();
    PDNDImpresa result =
        pdndInfoCamereRestClient.retrieveInstitutionPdndByTaxCode(taxCode, bearer).get(0);
    return pdndBusinessMapper.toPDNDBusiness(result);
  }

  public PDNDBusiness fallbackRetrieveInstitutionByTaxCode(RuntimeException e) {
    log.error(String.format(ERROR_PDND_INFO_CAMERE_REST_CLIENT_MESSAGE, e.getMessage()), e);
    throw new ResourceNotFoundException("");
  }
}
