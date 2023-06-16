package it.pagopa.selfcare.party.registry_proxy.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.party.registry_proxy.connector.api.NationalRegistriesConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.LegalAddressProfessionalResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.LegalAddressResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.VerifyLegalResponse;
import it.pagopa.selfcare.party.registry_proxy.core.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {NationalRegistriesServiceImpl.class})
@ExtendWith(SpringExtension.class)
class NationalRegistriesServiceImplTest {
    @MockBean
    private NationalRegistriesConnector nationalRegistriesConnector;

    @Autowired
    private NationalRegistriesServiceImpl nationalRegistriesServiceImpl;


    /**
     * Method under test: {@link NationalRegistriesServiceImpl#getLegalAddress(String)}
     */
    @Test
    void testGetLegalAddress() {
        LegalAddressProfessionalResponse legalAddressProfessionalResponse = new LegalAddressProfessionalResponse();
        legalAddressProfessionalResponse.setAddress("42 Main St");
        legalAddressProfessionalResponse.setDescription("The characteristics of someone or something");
        legalAddressProfessionalResponse.setMunicipality("Municipality");
        legalAddressProfessionalResponse.setProvince("Province");
        legalAddressProfessionalResponse.setZip("21654");

        LegalAddressProfessionalResponse legalAddressProfessionalResponse1 = new LegalAddressProfessionalResponse();
        legalAddressProfessionalResponse1.setAddress("42 Main St");
        legalAddressProfessionalResponse1.setDescription("The characteristics of someone or something");
        legalAddressProfessionalResponse1.setMunicipality("Municipality");
        legalAddressProfessionalResponse1.setProvince("Province");
        legalAddressProfessionalResponse1.setZip("21654");
        LegalAddressResponse legalAddressResponse = mock(LegalAddressResponse.class);
        when(legalAddressResponse.getProfessionalAddress()).thenReturn(legalAddressProfessionalResponse1);
        doNothing().when(legalAddressResponse).setDateTimeExtraction(any());
        doNothing().when(legalAddressResponse).setProfessionalAddress(any());
        doNothing().when(legalAddressResponse).setTaxId(any());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        legalAddressResponse.setDateTimeExtraction(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        legalAddressResponse.setProfessionalAddress(legalAddressProfessionalResponse);
        legalAddressResponse.setTaxId("42");
        when(nationalRegistriesConnector.getLegalAddress(any())).thenReturn(legalAddressResponse);
        LegalAddressProfessionalResponse actualLegalAddress = nationalRegistriesServiceImpl.getLegalAddress("Tax Id");
        assertEquals("42 Main St", actualLegalAddress.getAddress());
        assertEquals("21654", actualLegalAddress.getZip());
        assertEquals("Province", actualLegalAddress.getProvince());
        assertEquals("Municipality", actualLegalAddress.getMunicipality());
        assertEquals("The characteristics of someone or something", actualLegalAddress.getDescription());
        verify(nationalRegistriesConnector).getLegalAddress(any());
        verify(legalAddressResponse, atLeast(1)).getProfessionalAddress();
        verify(legalAddressResponse).setDateTimeExtraction(any());
        verify(legalAddressResponse).setProfessionalAddress(any());
        verify(legalAddressResponse).setTaxId(any());
    }

    /**
     * Method under test: {@link NationalRegistriesServiceImpl#verifyLegal(String, String)}
     */
    @Test
    void testVerifyLegal() {
        VerifyLegalResponse verifyLegalResponse = new VerifyLegalResponse();
        verifyLegalResponse.setVerificationResult(true);
        verifyLegalResponse.setVerifyLegalResultCode("Verify Legal Result Code");
        verifyLegalResponse.setVerifyLegalResultDetail("Verify Legal Result Detail");
        when(nationalRegistriesConnector.verifyLegal(any(), any())).thenReturn(verifyLegalResponse);
        assertSame(verifyLegalResponse, nationalRegistriesServiceImpl.verifyLegal("42", "42"));
        verify(nationalRegistriesConnector).verifyLegal(any(), any());
    }

    /**
     * Method under test: {@link NationalRegistriesServiceImpl#verifyLegal(String, String)}
     */
    @Test
    void testVerifyLegal2() {
        when(nationalRegistriesConnector.verifyLegal(any(), any()))
                .thenThrow(new ResourceNotFoundException());
        assertThrows(ResourceNotFoundException.class, () -> nationalRegistriesServiceImpl.verifyLegal("42", "42"));
        verify(nationalRegistriesConnector).verifyLegal(any(), any());
    }

    /**
     * Method under test: {@link InfoCamereServiceImpl#institutionsByLegalTaxId(String)}
     */
    @Test
    void testInstitutionsByLegalTaxId() {
        Businesses businesses = new Businesses();
        businesses.setBusinessList(new ArrayList<>());
        businesses.setLegalTaxId("42");
        businesses.setDateTimeExtraction("2020-03-01");
        when(nationalRegistriesConnector.institutionsByLegalTaxId(any())).thenReturn(businesses);

        assertSame(businesses, nationalRegistriesServiceImpl.institutionsByLegalTaxId("42"));
        verify(nationalRegistriesConnector).institutionsByLegalTaxId(any());
    }

    /**
     * Method under test: {@link InfoCamereServiceImpl#institutionsByLegalTaxId(String)}
     */
    @Test
    void testInstitutionsByLegalTaxIdNotFound() {
        Businesses businesses = new Businesses();
        businesses.setCode("WSPA_ERR_04");
        when(nationalRegistriesConnector.institutionsByLegalTaxId(any())).thenReturn(businesses);

        Businesses response = nationalRegistriesServiceImpl.institutionsByLegalTaxId("42");
        assertEquals("42", response.getLegalTaxId());
        assertEquals(0, response.getBusinessList().size());
        verify(nationalRegistriesConnector).institutionsByLegalTaxId(any());
    }

    /**
     * Method under test: {@link InfoCamereServiceImpl#institutionsByLegalTaxId(String)}
     */
    @Test
    void testInstitutionsByLegalTaxIdNotFound2() {
        Businesses businesses = new Businesses();
        businesses.setDescription("LR Not found");
        when(nationalRegistriesConnector.institutionsByLegalTaxId(any())).thenReturn(businesses);

        Businesses response = nationalRegistriesServiceImpl.institutionsByLegalTaxId("42");
        assertEquals("42", response.getLegalTaxId());
        assertEquals(0, response.getBusinessList().size());
        verify(nationalRegistriesConnector).institutionsByLegalTaxId(any());
    }

    /**
     * Method under test: {@link InfoCamereServiceImpl#institutionsByLegalTaxId(String)}
     */
    @Test
    void testInstitutionsByLegalTaxIdNotFound3() {
        Businesses businesses = new Businesses();
        businesses.setAppName("wspa-lrpf");
        when(nationalRegistriesConnector.institutionsByLegalTaxId(any())).thenReturn(businesses);

        Businesses response = nationalRegistriesServiceImpl.institutionsByLegalTaxId("42");
        assertEquals("42", response.getLegalTaxId());
        assertEquals(0, response.getBusinessList().size());
        verify(nationalRegistriesConnector).institutionsByLegalTaxId(any());
    }

    /**
     * Method under test: {@link InfoCamereServiceImpl#institutionsByLegalTaxId(String)}
     */
    @Test
    void testInstitutionsByLegalTaxIdNotFound4() {
        Businesses businesses = new Businesses();
        businesses.setTimestamp("2023-01-27T17:38:18.774");
        when(nationalRegistriesConnector.institutionsByLegalTaxId(any())).thenReturn(businesses);

        Businesses response = nationalRegistriesServiceImpl.institutionsByLegalTaxId("42");
        assertEquals("42", response.getLegalTaxId());
        assertEquals(0, response.getBusinessList().size());
        verify(nationalRegistriesConnector).institutionsByLegalTaxId(any());
    }
}

