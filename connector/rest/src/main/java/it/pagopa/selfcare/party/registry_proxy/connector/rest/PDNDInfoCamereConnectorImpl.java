package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.api.PDNDInfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDInfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDVisuraInfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.PDNDInfoCamereRestClientConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.PDNDVisuraInfoCamereRestClientConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.*;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.mapper.PDNDBusinessMapper;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.service.TokenProvider;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.service.TokenProviderPDND;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.service.TokenProviderVisura;
import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class PDNDInfoCamereConnectorImpl implements PDNDInfoCamereConnector {

  private final PDNDInfoCamereRestClient pdndInfoCamereRestClient;
  private final PDNDVisuraInfoCamereRestClient pdndVisuraInfoCamereRestClient;
  private final PDNDBusinessMapper pdndBusinessMapper;
  private final TokenProvider tokenProviderPDND;
  private final TokenProviderVisura tokenProviderVisura;
  private final PDNDInfoCamereRestClientConfig pdndInfoCamereRestClientConfig;
  private final PDNDVisuraInfoCamereRestClientConfig pdndVisuraInfoCamereRestClientConfig;
  private static final String BEARER = "Bearer ";

  public PDNDInfoCamereConnectorImpl(
          PDNDInfoCamereRestClient pdndInfoCamereRestClient,
          PDNDVisuraInfoCamereRestClient pdndVisuraInfoCamereRestClient,
          PDNDBusinessMapper pdndBusinessMapper,
          TokenProviderPDND tokenProviderPDND,
          TokenProviderVisura tokenProviderVisura,
          PDNDInfoCamereRestClientConfig pdndInfoCamereRestClientConfig,
          PDNDVisuraInfoCamereRestClientConfig pdndVisuraInfoCamereRestClientConfig) {
    this.pdndInfoCamereRestClient = pdndInfoCamereRestClient;
    this.pdndVisuraInfoCamereRestClient = pdndVisuraInfoCamereRestClient;
    this.pdndBusinessMapper = pdndBusinessMapper;
    this.tokenProviderPDND = tokenProviderPDND;
    this.tokenProviderVisura = tokenProviderVisura;
    this.pdndInfoCamereRestClientConfig = pdndInfoCamereRestClientConfig;
    this.pdndVisuraInfoCamereRestClientConfig = pdndVisuraInfoCamereRestClientConfig;
  }

  @Override
  public List<PDNDBusiness> retrieveInstitutionsPdndByDescription(String description) {
    Assert.hasText(description, "Description is required");
    ClientCredentialsResponse tokenResponse = tokenProviderPDND.getTokenPdnd(pdndInfoCamereRestClientConfig.getPdndSecretValue());
    String bearer = BEARER + tokenResponse.getAccessToken();
    List<PDNDImpresa> result = pdndInfoCamereRestClient.retrieveInstitutionsPdndByDescription(description, bearer);
    return pdndBusinessMapper.toPDNDBusinesses(result);
  }

  @Override
  public PDNDBusiness retrieveInstitutionPdndByTaxCode(String taxCode) {
    Assert.hasText(taxCode, "TaxCode is required");
    ClientCredentialsResponse tokenResponse = tokenProviderPDND.getTokenPdnd(pdndInfoCamereRestClientConfig.getPdndSecretValue());
    String bearer = BEARER + tokenResponse.getAccessToken();
    PDNDImpresa result = pdndInfoCamereRestClient.retrieveInstitutionPdndByTaxCode(taxCode, bearer).get(0);
    return pdndBusinessMapper.toPDNDBusiness(result);
  }

  @Override
  public PDNDBusiness retrieveInstitutionDetail(String taxCode) {
    Assert.hasText(taxCode, "TaxCode is required");
    ClientCredentialsResponse tokenResponse = tokenProviderVisura.getTokenPdnd(pdndVisuraInfoCamereRestClientConfig.getPdndSecretValue());
    String bearer = BEARER + tokenResponse.getAccessToken();
    PDNDVisuraImpresa result = pdndVisuraInfoCamereRestClient.retrieveInstitutionDetail(taxCode, bearer);
    return pdndBusinessMapper.toPDNDBusiness(result);
  }

  @Override
  public PDNDBusiness retrieveInstitutionFromRea(String county, String rea) {
    Assert.hasText(rea, "Rea is required");
    Assert.hasText(rea, "County is required");
    ClientCredentialsResponse tokenResponse = tokenProviderVisura.getTokenPdnd(pdndVisuraInfoCamereRestClientConfig.getPdndSecretValue());
    String bearer = BEARER + tokenResponse.getAccessToken();
    List<PDNDImpresa> institutions = pdndVisuraInfoCamereRestClient.retrieveInstitutionPdndFromRea(rea, county, bearer);
    if (Objects.isNull(institutions) || institutions.isEmpty()) {
      throw new ResourceNotFoundException("No institution found with rea: " + county + "-" + rea);
    }
    PDNDImpresa result  = institutions.get(0);
    PDNDVisuraImpresa visuraImpresa = pdndVisuraInfoCamereRestClient.retrieveInstitutionDetail(result.getBusinessTaxId(), bearer);
    return pdndBusinessMapper.toPDNDBusiness(visuraImpresa);
  }
}
