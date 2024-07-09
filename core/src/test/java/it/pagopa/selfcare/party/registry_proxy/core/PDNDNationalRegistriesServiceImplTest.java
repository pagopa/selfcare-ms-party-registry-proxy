package it.pagopa.selfcare.party.registry_proxy.core;


import it.pagopa.selfcare.party.registry_proxy.connector.api.PDNDNationalRegistriesConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PDNDNationalRegistriesServiceImpl.class})
class PDNDNationalRegistriesServiceImplTest {

    @Autowired
    private PDNDNationalRegistriesService pdndNationalRegistriesService;
    @MockBean
    private PDNDNationalRegistriesConnector pdndNationalRegistriesConnector;

    @Test
    void retrieveInstitutionsByDescription() {
        //given
        String description = "description";
        List<PDNDBusiness> pdndBusinesses = new ArrayList<>();
        pdndBusinesses.add(dummyPDNDBusiness());
        when(pdndNationalRegistriesConnector.retrieveInstitutionsPdndByDescription(anyString())).thenReturn(pdndBusinesses);

        //when
        pdndNationalRegistriesService.retrieveInstitutionsPdndByDescription(description);

        //then
        assertNotNull(pdndBusinesses);
        assertNotNull(pdndBusinesses.getClass());
        assertEquals(1, pdndBusinesses.size());
        verify(pdndNationalRegistriesConnector, times(1))
                .retrieveInstitutionsPdndByDescription(any());
        verifyNoMoreInteractions(pdndNationalRegistriesConnector);
    }

    @Test
    void retrieveInstitutionsByDescription_nullDescription() {
        //given
        String description = null;
        List<PDNDBusiness> pdndBusinesses = new ArrayList<>();
        pdndBusinesses.add(dummyPDNDBusiness());
        when(pdndNationalRegistriesConnector.retrieveInstitutionsPdndByDescription(any())).thenReturn(pdndBusinesses);

        //when
        Executable executable = () -> pdndNationalRegistriesService.retrieveInstitutionsPdndByDescription(description);

        //then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Description is required", e.getMessage());
        Mockito.verifyNoInteractions(pdndNationalRegistriesConnector);
    }

    @Test
    void retrieveInstitutionByTaxCode() {
        //given
        String taxCode = "taxCode";
        PDNDBusiness pdndBusiness = dummyPDNDBusiness();
        when(pdndNationalRegistriesConnector.retrieveInstitutionPdndByTaxCode(anyString())).thenReturn(pdndBusiness);

        //when
        pdndNationalRegistriesService.retrieveInstitutionPdndByTaxCode(taxCode);

        //then
        assertNotNull(pdndBusiness);
        assertNotNull(pdndBusiness.getClass());
        verify(pdndNationalRegistriesConnector, times(1))
                .retrieveInstitutionPdndByTaxCode(any());
        verifyNoMoreInteractions(pdndNationalRegistriesConnector);
    }

    @Test
    void retrieveInstitutionByTaxCode_nullTaxCode() {
        //given
        String taxCode = null;
        PDNDBusiness pdndBusiness = dummyPDNDBusiness();
        when(pdndNationalRegistriesConnector.retrieveInstitutionPdndByTaxCode(any())).thenReturn(pdndBusiness);

        //when
        Executable executable = () -> pdndNationalRegistriesService.retrieveInstitutionPdndByTaxCode(taxCode);

        //then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("TaxCode is required", e.getMessage());
        Mockito.verifyNoInteractions(pdndNationalRegistriesConnector);
    }

    private PDNDBusiness dummyPDNDBusiness(){
        PDNDBusiness pdndBusiness = new PDNDBusiness();
        pdndBusiness.setBusinessTaxId("taxId");
        pdndBusiness.setBusinessName("description");
        pdndBusiness.setBusinessStatus("status");
        pdndBusiness.setCity("city");
        pdndBusiness.setCciaa("cciaa");
        pdndBusiness.setAddress("address");
        pdndBusiness.setDigitalAddress("digitalAddress");
        pdndBusiness.setCounty("county");
        pdndBusiness.setLegalNature("legalNature");
        pdndBusiness.setLegalNatureDescription("legalNatureDescription");
        pdndBusiness.setNRea("nRea");
        pdndBusiness.setZipCode("zipCode");
        return pdndBusiness;
    }

}
