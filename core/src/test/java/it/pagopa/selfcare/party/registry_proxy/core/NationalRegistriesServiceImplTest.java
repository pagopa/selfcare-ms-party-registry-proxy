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
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.LegalAddressProfessionalResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.LegalAddressResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.VerifyLegalResponse;
import it.pagopa.selfcare.party.registry_proxy.core.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

        LegalAddressResponse legalAddressResponse = new LegalAddressResponse();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        legalAddressResponse.setDateTimeExtraction(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        legalAddressResponse.setProfessionalAddress(legalAddressProfessionalResponse);
        legalAddressResponse.setTaxId("42");
        when(nationalRegistriesConnector.getLegalAddress(any())).thenReturn(legalAddressResponse);
        LegalAddressProfessionalResponse actualLegalAddress = nationalRegistriesServiceImpl.getLegalAddress("42");
        assertEquals("42 Main St", actualLegalAddress.getAddress());
        assertEquals("21654", actualLegalAddress.getZip());
        assertEquals("Province", actualLegalAddress.getProvince());
        assertEquals("Municipality", actualLegalAddress.getMunicipality());
        assertEquals("The characteristics of someone or something", actualLegalAddress.getDescription());
        verify(nationalRegistriesConnector).getLegalAddress(any());
    }

    /**
     * Method under test: {@link NationalRegistriesServiceImpl#getLegalAddress(String)}
     */
    @Test
    void testGetLegalAddress2() {
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
     * Method under test: {@link NationalRegistriesServiceImpl#getLegalAddress(String)}
     */
    @Test
    void testGetLegalAddress3() {
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
        LegalAddressProfessionalResponse actualLegalAddress = nationalRegistriesServiceImpl.getLegalAddress("");
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
     * Method under test: {@link NationalRegistriesServiceImpl#getLegalAddress(String)}
     */
    @Test
    void testGetLegalAddress4() {
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
        LegalAddressProfessionalResponse actualLegalAddress = nationalRegistriesServiceImpl.getLegalAddress("4242");
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
        VerifyLegalResponse verifyLegalResponse = new VerifyLegalResponse();
        verifyLegalResponse.setVerificationResult(true);
        verifyLegalResponse.setVerifyLegalResultCode("Verify Legal Result Code");
        verifyLegalResponse.setVerifyLegalResultDetail("Verify Legal Result Detail");
        when(nationalRegistriesConnector.verifyLegal(any(), any())).thenReturn(verifyLegalResponse);
        assertSame(verifyLegalResponse,
                nationalRegistriesServiceImpl.verifyLegal("verify legal {} for vatNumber: {}", "42"));
        verify(nationalRegistriesConnector).verifyLegal(any(), any());
    }

    /**
     * Method under test: {@link NationalRegistriesServiceImpl#verifyLegal(String, String)}
     */
    @Test
    void testVerifyLegal3() {
        VerifyLegalResponse verifyLegalResponse = new VerifyLegalResponse();
        verifyLegalResponse.setVerificationResult(true);
        verifyLegalResponse.setVerifyLegalResultCode("Verify Legal Result Code");
        verifyLegalResponse.setVerifyLegalResultDetail("Verify Legal Result Detail");
        when(nationalRegistriesConnector.verifyLegal(any(), any())).thenReturn(verifyLegalResponse);
        assertSame(verifyLegalResponse, nationalRegistriesServiceImpl.verifyLegal("", "42"));
        verify(nationalRegistriesConnector).verifyLegal(any(), any());
    }

    /**
     * Method under test: {@link NationalRegistriesServiceImpl#verifyLegal(String, String)}
     */
    @Test
    void testVerifyLegal4() {
        when(nationalRegistriesConnector.verifyLegal(any(), any()))
                .thenThrow(new ResourceNotFoundException());
        assertThrows(ResourceNotFoundException.class, () -> nationalRegistriesServiceImpl.verifyLegal("42", "42"));
        verify(nationalRegistriesConnector).verifyLegal(any(), any());
    }
}

