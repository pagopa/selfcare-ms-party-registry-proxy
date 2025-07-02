package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDInfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.PDNDInfoCamereRestClientConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.ClientCredentialsResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PDNDImpresa;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PDNDSedeImpresa;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.mapper.PDNDBusinessMapper;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.service.TokenProviderPDND;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class PDNDInfoCamereConnectorImplTest {

  @InjectMocks private PDNDInfoCamereConnectorImpl pdndInfoCamereConnector;
  @Mock private PDNDInfoCamereRestClient pdndInfoCamereRestClient;
  @Mock private TokenProviderPDND tokenProvider;
  @Mock private PDNDBusinessMapper pdndBusinessMapper;
  @Mock private PDNDInfoCamereRestClientConfig pdndInfoCamereRestClientConfig;

  @Test
  void testRetrieveInstitutionsByDescription() {

    // given
    String description = "description";
    List<PDNDBusiness> pdndBusinesses = new ArrayList<>();
    pdndBusinesses.add(dummyPDNDBusiness());
    List<PDNDImpresa> pdndImpresaList = new ArrayList<>();
    pdndImpresaList.add(dummyPDNDImpresa());

    mockPdndSecretValue();
    mockPdndToken();
    when(pdndInfoCamereRestClient.retrieveInstitutionsPdndByDescription(anyString(), anyString()))
        .thenReturn(pdndImpresaList);
    when(pdndBusinessMapper.toPDNDBusinesses(pdndImpresaList)).thenReturn(pdndBusinesses);

    // when
    pdndBusinesses = pdndInfoCamereConnector.retrieveInstitutionsPdndByDescription(description);

    // then
    assertNotNull(pdndBusinesses);
    assertNotNull(pdndBusinesses.getClass());
    assertEquals(1, pdndBusinesses.size());
    PDNDBusiness pdndBusiness = pdndBusinesses.iterator().next();
    assertEquals(dummyPDNDImpresa().getBusinessTaxId(), pdndBusiness.getBusinessTaxId());
    assertEquals(dummyPDNDImpresa().getBusinessName(), pdndBusiness.getBusinessName());
    assertEquals(dummyPDNDImpresa().getBusinessStatus(), pdndBusiness.getBusinessStatus());
    assertEquals(dummyPDNDImpresa().getLegalNature(), pdndBusiness.getLegalNature());
    assertEquals(
        dummyPDNDImpresa().getLegalNatureDescription(), pdndBusiness.getLegalNatureDescription());
    assertEquals(dummyPDNDImpresa().getAddress(), pdndBusiness.getAddress());
    assertEquals(dummyPDNDImpresa().getDigitalAddress(), pdndBusiness.getDigitalAddress());
    assertEquals(dummyPDNDImpresa().getNRea(), pdndBusiness.getNRea());
    assertEquals(dummyPDNDImpresa().getCciaa(), pdndBusiness.getCciaa());
    assertEquals(dummyPDNDImpresa().getBusinessAddress().getCity(), pdndBusiness.getCity());
    assertEquals(dummyPDNDImpresa().getBusinessAddress().getCounty(), pdndBusiness.getCounty());
    assertEquals(dummyPDNDImpresa().getBusinessAddress().getZipCode(), pdndBusiness.getZipCode());

    verify(pdndInfoCamereRestClient, times(1))
        .retrieveInstitutionsPdndByDescription(anyString(), anyString());
    verifyNoMoreInteractions(pdndInfoCamereRestClient);
  }

  @Test
  void testRetrieveInstitutionsByDescription_nullDescription() {

    // given
    String description = null;
    List<PDNDImpresa> pdndImpresaList = new ArrayList<>();
    pdndImpresaList.add(dummyPDNDImpresa());

    when(pdndInfoCamereRestClient.retrieveInstitutionsPdndByDescription(anyString(), anyString()))
        .thenReturn(pdndImpresaList);

    // when
    Executable executable =
        () -> pdndInfoCamereConnector.retrieveInstitutionsPdndByDescription(description);

    // then
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Description is required", e.getMessage());
    Mockito.verifyNoInteractions(pdndInfoCamereRestClient);
  }

  @Test
  void testRetrieveInstitutionByTaxCode() {

    // given
    String taxCode = "taxCode";
    PDNDBusiness pdndBusiness = dummyPDNDBusiness();
    List<PDNDImpresa> pdndImpresaList = new ArrayList<>();
    pdndImpresaList.add(dummyPDNDImpresa());

    mockPdndToken();
    mockPdndSecretValue();
    when(pdndInfoCamereRestClient.retrieveInstitutionPdndByTaxCode(anyString(), anyString()))
        .thenReturn(pdndImpresaList);
    when(pdndBusinessMapper.toPDNDBusiness(dummyPDNDImpresa())).thenReturn(pdndBusiness);

    // when
    pdndBusiness = pdndInfoCamereConnector.retrieveInstitutionPdndByTaxCode(taxCode);

    // then
    assertNotNull(pdndBusiness);
    assertNotNull(pdndBusiness.getClass());
    assertEquals(1, pdndImpresaList.size());
    PDNDImpresa pdndImpresa = pdndImpresaList.iterator().next();
    assertEquals(pdndImpresa.getBusinessTaxId(), pdndBusiness.getBusinessTaxId());
    assertEquals(pdndImpresa.getBusinessName(), pdndBusiness.getBusinessName());
    assertEquals(pdndImpresa.getBusinessStatus(), pdndBusiness.getBusinessStatus());
    assertEquals(pdndImpresa.getLegalNature(), pdndBusiness.getLegalNature());
    assertEquals(pdndImpresa.getLegalNatureDescription(), pdndBusiness.getLegalNatureDescription());
    assertEquals(pdndImpresa.getAddress(), pdndBusiness.getAddress());
    assertEquals(pdndImpresa.getDigitalAddress(), pdndBusiness.getDigitalAddress());
    assertEquals(pdndImpresa.getNRea(), pdndBusiness.getNRea());
    assertEquals(pdndImpresa.getCciaa(), pdndBusiness.getCciaa());
    assertEquals(pdndImpresa.getBusinessAddress().getCity(), pdndBusiness.getCity());
    assertEquals(pdndImpresa.getBusinessAddress().getCounty(), pdndBusiness.getCounty());
    assertEquals(pdndImpresa.getBusinessAddress().getZipCode(), pdndBusiness.getZipCode());

    verify(pdndInfoCamereRestClient, times(1))
        .retrieveInstitutionPdndByTaxCode(anyString(), anyString());
    verifyNoMoreInteractions(pdndInfoCamereRestClient);
  }

  @Test
  void testRetrieveInstitutionByTaxCode_nullTaxCode() {

    // given
    String taxCode = null;
    List<PDNDImpresa> pdndImpresaList = new ArrayList<>();
    pdndImpresaList.add(dummyPDNDImpresa());

    when(pdndInfoCamereRestClient.retrieveInstitutionPdndByTaxCode(anyString(), anyString()))
        .thenReturn(pdndImpresaList);

    // when
    Executable executable = () -> pdndInfoCamereConnector.retrieveInstitutionPdndByTaxCode(taxCode);

    // then
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("TaxCode is required", e.getMessage());
    Mockito.verifyNoInteractions(pdndInfoCamereRestClient);
  }

  private PDNDBusiness dummyPDNDBusiness() {
    PDNDBusiness pdndBusiness = new PDNDBusiness();
    pdndBusiness.setBusinessTaxId("12345678901");
    pdndBusiness.setBusinessName("Dummy Business Name");
    pdndBusiness.setBusinessStatus("Active");
    pdndBusiness.setCity("Milano");
    pdndBusiness.setCciaa("MI123456");
    pdndBusiness.setAddress("Via Montenapoleone 10");
    pdndBusiness.setDigitalAddress("dummy@example.com");
    pdndBusiness.setCounty("MI");
    pdndBusiness.setLegalNature("LLC");
    pdndBusiness.setLegalNatureDescription("Limited Liability Company");
    pdndBusiness.setNRea("MI67890");
    pdndBusiness.setZipCode("20100");
    return pdndBusiness;
  }

  public PDNDImpresa dummyPDNDImpresa() {
    PDNDImpresa pdndImpresa = new PDNDImpresa();
    pdndImpresa.setBusinessTaxId("12345678901");
    pdndImpresa.setBusinessName("Dummy Business Name");
    pdndImpresa.setLegalNature("LLC");
    pdndImpresa.setLegalNatureDescription("Limited Liability Company");
    pdndImpresa.setCciaa("MI123456");
    pdndImpresa.setNRea("MI67890");
    pdndImpresa.setBusinessStatus("Active");
    pdndImpresa.setBusinessAddress(dummyPDNDSedeImpresa());
    pdndImpresa.setDigitalAddress("dummy@example.com");
    return pdndImpresa;
  }

  public PDNDSedeImpresa dummyPDNDSedeImpresa() {
    PDNDSedeImpresa pdndSedeImpresa = new PDNDSedeImpresa();
    pdndSedeImpresa.setToponimoSede("Via");
    pdndSedeImpresa.setViaSede("Montenapoleone");
    pdndSedeImpresa.setNcivicoSede("10");
    pdndSedeImpresa.setZipCode("20100");
    pdndSedeImpresa.setCity("Milano");
    pdndSedeImpresa.setCounty("MI");
    return pdndSedeImpresa;
  }

  private void mockPdndToken() {
    ClientCredentialsResponse clientCredentialsResponse = new ClientCredentialsResponse();
    clientCredentialsResponse.setAccessToken("accessToken");
    when(tokenProvider.getTokenPdnd(any())).thenReturn(clientCredentialsResponse);
  }

  private void mockPdndSecretValue() {
    when(pdndInfoCamereRestClientConfig.getPdndSecretValue()).thenReturn(null);
  }
}
