package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.api.PDNDInfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDInfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDInfoCamereVisuraRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.PDNDInfoCamereRestClientConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.*;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.mapper.PDNDBusinessMapper;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.service.TokenProviderPDND;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.service.TokenProviderVisura;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class PDNDInfoCamereConnectorImpl implements PDNDInfoCamereConnector {

  private final PDNDInfoCamereRestClient pdndInfoCamereRestClient;
  private final PDNDInfoCamereVisuraRestClient pdndInfoCamereVisuraRestClient;
  private final PDNDBusinessMapper pdndBusinessMapper;
  private final TokenProviderPDND tokenProviderPDND;
  private final TokenProviderVisura tokenProviderVisura;
  private final PDNDInfoCamereRestClientConfig pdndInfoCamereRestClientConfig;

  public PDNDInfoCamereConnectorImpl(
          PDNDInfoCamereRestClient pdndInfoCamereRestClient,
          PDNDInfoCamereVisuraRestClient pdndInfoCamereVisuraRestClient,
          PDNDBusinessMapper pdndBusinessMapper,
          TokenProviderPDND tokenProviderPDND,
          TokenProviderVisura tokenProviderVisura,
          PDNDInfoCamereRestClientConfig pdndInfoCamereRestClientConfig) {
    this.pdndInfoCamereRestClient = pdndInfoCamereRestClient;
    this.pdndInfoCamereVisuraRestClient = pdndInfoCamereVisuraRestClient;
    this.pdndBusinessMapper = pdndBusinessMapper;
    this.tokenProviderPDND = tokenProviderPDND;
    this.tokenProviderVisura = tokenProviderVisura;
    this.pdndInfoCamereRestClientConfig = pdndInfoCamereRestClientConfig;
  }

  @Override
  public List<PDNDBusiness> retrieveInstitutionsPdndByDescription(String description) {
    Assert.hasText(description, "Description is required");
    ClientCredentialsResponse tokenResponse = tokenProviderPDND.getTokenPdnd(pdndInfoCamereRestClientConfig.getPdndSecretValue());
    String bearer = "Bearer " + tokenResponse.getAccessToken();
    List<PDNDImpresa> result = pdndInfoCamereRestClient.retrieveInstitutionsPdndByDescription(description, bearer);
    return pdndBusinessMapper.toPDNDBusinesses(result);
  }

  @Override
  public PDNDBusiness retrieveInstitutionPdndByTaxCode(String taxCode) {
    Assert.hasText(taxCode, "TaxCode is required");
    ClientCredentialsResponse tokenResponse = tokenProviderPDND.getTokenPdnd(pdndInfoCamereRestClientConfig.getPdndSecretValue());
    String bearer = "Bearer " + tokenResponse.getAccessToken();
    PDNDImpresa result = pdndInfoCamereRestClient.retrieveInstitutionPdndByTaxCode(taxCode, bearer).get(0);
    return pdndBusinessMapper.toPDNDBusiness(result);
  }

  @Override
  public PDNDBusiness retrieveInstitutionDetail(String taxCode) {
    Assert.hasText(taxCode, "TaxCode is required");
    ClientCredentialsResponse tokenResponse = tokenProviderVisura.getTokenPdnd(pdndInfoCamereRestClientConfig.getPdndSecretValue());
    String bearer = "Bearer " + tokenResponse.getAccessToken();
    PDNDVisuraImpresa result = pdndInfoCamereVisuraRestClient.retrieveInstitutionPdnd(taxCode, bearer);
    return pdndBusinessMapper.toPDNDBusiness(result);
  }

  @Override
  public PDNDBusiness retrieveInstitutionFromRea(String rea, String county) {
    Assert.hasText(rea, "Rea is required");
    Assert.hasText(rea, "County is required");
    ClientCredentialsResponse tokenResponse = tokenProviderVisura.getTokenPdnd(pdndInfoCamereRestClientConfig.getPdndSecretValue());
    String bearer = "Bearer " + tokenResponse.getAccessToken();
    PDNDImpresa result = pdndInfoCamereVisuraRestClient.retrieveInstitutionPdndFromRea(rea, county, bearer).get(0);
    return pdndBusinessMapper.toPDNDBusiness(result);
  }
}
