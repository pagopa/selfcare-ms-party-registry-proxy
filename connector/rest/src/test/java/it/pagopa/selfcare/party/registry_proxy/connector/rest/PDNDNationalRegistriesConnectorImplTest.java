package it.pagopa.selfcare.party.registry_proxy.connector.rest;



import it.pagopa.selfcare.party.registry_proxy.connector.api.PDNDNationalRegistriesConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ServiceUnavailableException;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GeographicTaxonomy;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDNationalRegistriesRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PDNDImpresa;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.geotaxonomy.GeographicTaxonomiesResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.geotaxonomy.GeographicTaxonomyResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.mapper.PDNDBusinessMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class PDNDNationalRegistriesConnectorImplTest {
    @InjectMocks
    private PDNDNationalRegistriesConnectorImpl pdndNationalRegistriesConnector;
    @Mock
    private PDNDNationalRegistriesRestClient pdndNationalRegistriesRestClient;
    @Mock
    private PDNDBusinessMapper pdndBusinessMapper;


    @Test
    void testRetrieveInstitutionsByDescription() {

        //given
        String description = "description";
        List<PDNDBusiness> pdndBusinesses = new ArrayList<>();
        pdndBusinesses.add(dummyPDNDBusiness());
        List<PDNDImpresa> pdndImpresaList = new ArrayList<>();
        pdndImpresaList.add(dummyPDNDImpresa());

        when(pdndNationalRegistriesRestClient.retrieveInstitutionsPdndByDescription(anyString())).thenReturn(pdndImpresaList);
        when(pdndBusinessMapper.toPDNDBusiness(pdndImpresaList)).thenReturn(pdndBusinesses);

        //when
        pdndBusinesses = pdndNationalRegistriesConnector.retrieveInstitutionsPdndByDescription(description);

        //then
        assertNotNull(pdndBusinesses);
        assertNotNull(pdndBusinesses.getClass());
        assertEquals(1, pdndBusinesses.size());
        PDNDBusiness pdndBusiness = pdndBusinesses.iterator().next();
        assertEquals(dummyPDNDImpresa().getBusinessTaxId(), pdndBusiness.getBusinessTaxId());
        assertEquals(dummyPDNDImpresa().getBusinessName(), pdndBusiness.getBusinessName());
        assertEquals(dummyPDNDImpresa().getBusinessStatus(), pdndBusiness.getBusinessStatus());
        assertEquals(dummyPDNDImpresa().getLegalNature(), pdndBusiness.getLegalNature());
        assertEquals(dummyPDNDImpresa().getLegalNatureDescription(), pdndBusiness.getLegalNatureDescription());
        assertEquals(dummyPDNDImpresa().getAddress(), pdndBusiness.getAddress());
        assertEquals(dummyPDNDImpresa().getDigitalAddress(), pdndBusiness.getDigitalAddress());
        assertEquals(dummyPDNDImpresa().getNRea(), pdndBusiness.getNRea());
        assertEquals(dummyPDNDImpresa().getCciaa(), pdndBusiness.getCciaa());
        assertEquals(dummyPDNDImpresa().getCity(), pdndBusiness.getCity());
        assertEquals(dummyPDNDImpresa().getCounty(), pdndBusiness.getCounty());
        assertEquals(dummyPDNDImpresa().getZipCode(), pdndBusiness.getZipCode());

        verify(pdndNationalRegistriesRestClient, times(1))
                .retrieveInstitutionsPdndByDescription(anyString());
        verifyNoMoreInteractions(pdndNationalRegistriesRestClient);

    }

    @Test
    void testRetrieveInstitutionsByDescription_nullDescription() {

        //given
        String description = null;
        List<PDNDImpresa> pdndImpresaList = new ArrayList<>();
        pdndImpresaList.add(dummyPDNDImpresa());

        when(pdndNationalRegistriesRestClient.retrieveInstitutionsPdndByDescription(anyString())).thenReturn(pdndImpresaList);

        //when
        Executable executable = () -> pdndNationalRegistriesConnector.retrieveInstitutionsPdndByDescription(description);

        //then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Description is required", e.getMessage());
        Mockito.verifyNoInteractions(pdndNationalRegistriesRestClient);

    }

    @Test
    void fallbackGetExtByDescriptionTest(){
        List<PDNDBusiness> list = pdndNationalRegistriesConnector.fallbackRetrieveInstitutionByDescription(new ServiceUnavailableException());
        Assertions.assertTrue(list.isEmpty());
    }

    private PDNDBusiness dummyPDNDBusiness(){
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
        pdndImpresa.setCity("Milano");
        pdndImpresa.setCounty("MI");
        pdndImpresa.setZipCode("20100");
        pdndImpresa.setDigitalAddress("dummy@example.com");
        pdndImpresa.setToponimoSede("Via");
        pdndImpresa.setViaSede("Montenapoleone");
        pdndImpresa.setNcivicoSede("10");
        return pdndImpresa;
    }

}

