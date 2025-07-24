package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.connector.model.national_registries_pdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDInfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDVisuraInfoCamereRawRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDVisuraInfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.PDNDInfoCamereRestClientConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.PDNDVisuraInfoCamereRestClientConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.*;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.mapper.PDNDBusinessMapper;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.visura.DatiIdentificativiImpresa;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.service.TokenProviderPDND;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.service.TokenProviderVisura;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PDNDInfoCamereConnectorImplTest {

  @InjectMocks private PDNDInfoCamereConnectorImpl pdndInfoCamereConnector;
  @Mock private PDNDInfoCamereRestClient pdndInfoCamereRestClient;
  @Mock private PDNDVisuraInfoCamereRestClient pdndVisuraInfoCamereRestClient;
  @Mock private PDNDVisuraInfoCamereRawRestClient pdndVisuraInfoCamereRawRestClient;
  @Mock private TokenProviderPDND tokenProviderPDND;
  @Mock private TokenProviderVisura tokenProviderVisura;
  @Mock private PDNDBusinessMapper pdndBusinessMapper;
  @Mock private PDNDInfoCamereRestClientConfig pdndInfoCamereRestClientConfig;
  @Mock private PDNDVisuraInfoCamereRestClientConfig pdndVisuraInfoCamereRestClientConfig;

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
  void testRetrieveInstitutionsByRea() {

    // given
    final String rea = "rea";
    final String county = "county";
    PDNDBusiness pdndBusiness = dummyPDNDBusiness();
    PDNDImpresa pdndImpresa = dummyPDNDImpresa();
    PDNDVisuraImpresa pdndVisuraImpresa = dummyPDNDVisuraImpresa();

    mockPdndVisuraSecretValue();
    mockPdndVisuraToken();
    when(pdndVisuraInfoCamereRestClient.retrieveInstitutionPdndFromRea(anyString(), anyString(), anyString()))
            .thenReturn(List.of(pdndImpresa));
    when(pdndVisuraInfoCamereRestClient.retrieveInstitutionDetail(anyString(), anyString()))
            .thenReturn(pdndVisuraImpresa);
    when(pdndBusinessMapper.toPDNDBusiness(pdndVisuraImpresa)).thenReturn(pdndBusiness);

    // when
    PDNDBusiness result = pdndInfoCamereConnector.retrieveInstitutionFromRea(rea, county);

    // then
    assertNotNull(result);
    assertEquals(dummyPDNDImpresa().getBusinessTaxId(), result.getBusinessTaxId());
    assertEquals(dummyPDNDImpresa().getBusinessName(), result.getBusinessName());
    assertEquals(dummyPDNDImpresa().getBusinessStatus(), result.getBusinessStatus());
    assertEquals(dummyPDNDImpresa().getLegalNature(), result.getLegalNature());
    assertEquals(
            dummyPDNDImpresa().getLegalNatureDescription(), result.getLegalNatureDescription());
    assertEquals(dummyPDNDImpresa().getAddress(), result.getAddress());
    assertEquals(dummyPDNDImpresa().getDigitalAddress(), result.getDigitalAddress());
    assertEquals(dummyPDNDImpresa().getNRea(), result.getNRea());
    assertEquals(dummyPDNDImpresa().getCciaa(), result.getCciaa());
    assertEquals(dummyPDNDImpresa().getBusinessAddress().getCity(), result.getCity());
    assertEquals(dummyPDNDImpresa().getBusinessAddress().getCounty(), result.getCounty());
    assertEquals(dummyPDNDImpresa().getBusinessAddress().getZipCode(), result.getZipCode());

    verify(pdndVisuraInfoCamereRestClient, times(1))
            .retrieveInstitutionPdndFromRea(anyString(), anyString(), anyString());
    verify(pdndVisuraInfoCamereRestClient, times(1))
            .retrieveInstitutionDetail(anyString(), anyString());
    verifyNoMoreInteractions(pdndVisuraInfoCamereRestClient);
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
  void testRetrieveInstitutionsByRea_nullRea() {

    // given
    final String rea = null;
    List<PDNDImpresa> pdndImpresaList = new ArrayList<>();
    pdndImpresaList.add(dummyPDNDImpresa());

    when(pdndVisuraInfoCamereRestClient.retrieveInstitutionPdndFromRea(anyString(), anyString(), anyString()))
            .thenReturn(pdndImpresaList);

    // when
    Executable executable =
            () -> pdndInfoCamereConnector.retrieveInstitutionFromRea("county", rea);

    // then
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Rea is required", e.getMessage());
    Mockito.verifyNoInteractions(pdndVisuraInfoCamereRestClient);
  }

  @Test
  void testRetrieveInstitutionsByRea_notFound() {

    // given
    final String rea = "test";
    final String county = "county";
    when(pdndVisuraInfoCamereRestClient.retrieveInstitutionPdndFromRea(
            anyString(), anyString(), anyString()))
        .thenReturn(Collections.emptyList());

    mockPdndVisuraToken();
    mockPdndVisuraSecretValue();
    // when
    Executable executable =
            () -> pdndInfoCamereConnector.retrieveInstitutionFromRea(county, rea);

    // then
    ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
    assertEquals("No institution found with rea: " + county + "-" + rea, e.getMessage());
  }

  @Test
  void testRetrieveInstitutionsByRea_notFoundAndNull() {

    // given
    final String rea = "test";
    final String county = "county";
    when(pdndVisuraInfoCamereRestClient.retrieveInstitutionPdndFromRea(
            anyString(), anyString(), anyString()))
            .thenReturn(null);

    mockPdndVisuraToken();
    mockPdndVisuraSecretValue();
    // when
    Executable executable =
            () -> pdndInfoCamereConnector.retrieveInstitutionFromRea(county, rea);

    // then
    ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
    assertEquals("No institution found with rea: " + county + "-" + rea, e.getMessage());
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
  void testRetrieveInstitutionDetail() {

    // given
    final String taxCode = "taxCode";
    PDNDBusiness pdndBusiness = dummyPDNDBusiness();
    PDNDVisuraImpresa pdndVisuraImpresa = dummyPDNDVisuraImpresa();

    mockPdndVisuraToken();
    mockPdndVisuraSecretValue();
    when(pdndVisuraInfoCamereRestClient.retrieveInstitutionDetail(anyString(), anyString()))
            .thenReturn(pdndVisuraImpresa);
    when(pdndBusinessMapper.toPDNDBusiness(pdndVisuraImpresa)).thenReturn(pdndBusiness);

    // when
    var result = pdndInfoCamereConnector.retrieveInstitutionDetail(taxCode);

    // then
    assertNotNull(pdndBusiness);
    assertEquals(pdndVisuraImpresa.getDatiIdentificativiImpresa().getBusinessTaxId(), result.getBusinessTaxId());
    assertEquals(pdndVisuraImpresa.getDatiIdentificativiImpresa().getNRea(), result.getNRea());
    assertEquals(pdndVisuraImpresa.getDatiIdentificativiImpresa().getCciaa(), result.getCciaa());
    assertEquals(pdndVisuraImpresa.getDatiIdentificativiImpresa().getDigitalAddress(), result.getDigitalAddress());

    verify(pdndVisuraInfoCamereRestClient, times(1))
            .retrieveInstitutionDetail(anyString(), anyString());
    verifyNoMoreInteractions(pdndVisuraInfoCamereRestClient);
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

  @Test
  void testRetrieveInstitutionDocument() {

    // given
    final String taxCode = "taxCode";
    final String document =
            """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <Impresa>
                        <Denominazione>ABC S.p.A.</Denominazione>
                        <PartitaIVA>01234567890</PartitaIVA>
                        <CodiceFiscale>ABCDEF12G34H567I</CodiceFiscale>
                        <Indirizzo>
                            <Via>Via Roma 1</Via>
                            <CAP>00100</CAP>
                            <Comune>Roma</Comune>
                        </Indirizzo>
                        <NodoDaRimuovere1>
                            <Campo>Valore1</Campo>
                        </NodoDaRimuovere1>
                        <NodoDaRimuovere2>
                            <Campo>Valore2</Campo>
                        </NodoDaRimuovere2>
                    </Impresa>""";
    // given
    mockPdndVisuraToken();
    mockPdndVisuraSecretValue();
    when(pdndVisuraInfoCamereRawRestClient.getRawInstitutionDetail(anyString(), anyString()))
            .thenReturn(document.getBytes(StandardCharsets.UTF_8));
    // when
    var result = pdndInfoCamereConnector.retrieveInstitutionDocument(taxCode);

    // then
    assertNotNull(result);
    assertNotEquals(0, result.length);

    verify(pdndVisuraInfoCamereRawRestClient, times(1))
            .getRawInstitutionDetail(anyString(), anyString());
    verifyNoMoreInteractions(pdndVisuraInfoCamereRawRestClient);
  }

  @Test
  void testRetrieveInstitutionDocument_withNodeToRemove() {

    // given
    final String taxCode = "taxCode";
    final String document =
            """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <Impresa>
                        <Denominazione>ABC S.p.A.</Denominazione>
                        <PartitaIVA>01234567890</PartitaIVA>
                        <CodiceFiscale>ABCDEF12G34H567I</CodiceFiscale>
                        <Indirizzo>
                            <Via>Via Roma 1</Via>
                            <CAP>00100</CAP>
                            <Comune>Roma</Comune>
                        </Indirizzo>
                        <elenco-soci>
                            <Campo>Valore1</Campo>
                        </elenco-soci>
                    </Impresa>""";
    // given
    mockPdndVisuraToken();
    mockPdndVisuraSecretValue();
    when(pdndVisuraInfoCamereRawRestClient.getRawInstitutionDetail(anyString(), anyString()))
            .thenReturn(document.getBytes(StandardCharsets.UTF_8));
    // when
    var result = pdndInfoCamereConnector.retrieveInstitutionDocument(taxCode);

    // then
    assertNotNull(result);
    assertNotEquals(0, result.length);

    String cleanedXml = new String(result, StandardCharsets.UTF_8);
    assertFalse(cleanedXml.contains("<elenco-soci>"), "L'XML non dovrebbe contenere <elenco-soci>");

    verify(pdndVisuraInfoCamereRawRestClient, times(1))
            .getRawInstitutionDetail(anyString(), anyString());
    verifyNoMoreInteractions(pdndVisuraInfoCamereRawRestClient);
  }

  @Test
  void testRetrieveInstitutionDocument_throws() {

    // given
    final String taxCode = "taxCode";
    final var document = "document";

    // given
    mockPdndVisuraToken();
    mockPdndVisuraSecretValue();
    when(pdndVisuraInfoCamereRawRestClient.getRawInstitutionDetail(anyString(), anyString()))
            .thenReturn(document.getBytes(StandardCharsets.UTF_8));

    // when
    Executable executable = () -> pdndInfoCamereConnector.retrieveInstitutionDocument(taxCode);

    // then
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class, executable);
    assertEquals(
        "Impossible to parse document for institution with taxCode: " + taxCode, e.getMessage());
    verify(pdndVisuraInfoCamereRawRestClient, times(1))
            .getRawInstitutionDetail(anyString(), anyString());
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

  public PDNDVisuraImpresa dummyPDNDVisuraImpresa() {
    PDNDVisuraImpresa pdndImpresa = new PDNDVisuraImpresa();
    DatiIdentificativiImpresa datiIdentificativiImpresa = new DatiIdentificativiImpresa();
    datiIdentificativiImpresa.setBusinessTaxId("12345678901");
    datiIdentificativiImpresa.setCciaa("MI123456");
    datiIdentificativiImpresa.setBusinessName("Dummy Business Name");
    datiIdentificativiImpresa.setNRea("MI67890");
    datiIdentificativiImpresa.setDigitalAddress("dummy@example.com");
    pdndImpresa.setDatiIdentificativiImpresa(datiIdentificativiImpresa);
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
    when(tokenProviderPDND.getTokenPdnd(any())).thenReturn(clientCredentialsResponse);
  }

  private void mockPdndVisuraToken() {
    ClientCredentialsResponse clientCredentialsResponse = new ClientCredentialsResponse();
    clientCredentialsResponse.setAccessToken("accessToken");
    when(tokenProviderVisura.getTokenPdnd(any())).thenReturn(clientCredentialsResponse);
  }

  private void mockPdndSecretValue() {
    when(pdndInfoCamereRestClientConfig.getPdndSecretValue()).thenReturn(null);
  }

  private void mockPdndVisuraSecretValue() {
    when(pdndVisuraInfoCamereRestClientConfig.getPdndSecretValue()).thenReturn(null);
  }
}
