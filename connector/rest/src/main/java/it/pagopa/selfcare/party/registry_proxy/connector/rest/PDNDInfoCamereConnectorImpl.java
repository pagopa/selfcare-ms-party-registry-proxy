package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.api.PDNDInfoCamereConnector;
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
  public List<PDNDBusiness> retrieveInstitutionsPdndByDescription(String description) {
    Assert.hasText(description, "Description is required");
    ClientCredentialsResponse tokenResponse =
        tokenProvider.getTokenPdnd(pdndInfoCamereRestClientConfig.getPdndSecretValue());
    String bearer = "Bearer " + tokenResponse.getAccessToken();
    List<PDNDImpresa> result =
        pdndInfoCamereRestClient.retrieveInstitutionsPdndByDescription(description, bearer);
    return pdndBusinessMapper.toPDNDBusinesses(result);
  }

  @Override
  public PDNDBusiness retrieveInstitutionPdndByTaxCode(String taxCode) {
    Assert.hasText(taxCode, "TaxCode is required");
    ClientCredentialsResponse tokenResponse =
        tokenProvider.getTokenPdnd(pdndInfoCamereRestClientConfig.getPdndSecretValue());
    String bearer = "Bearer " + tokenResponse.getAccessToken();
    PDNDImpresa result =
        pdndInfoCamereRestClient.retrieveInstitutionPdndByTaxCode(taxCode, bearer).get(0);
    return pdndBusinessMapper.toPDNDBusiness(result);
  }
}
