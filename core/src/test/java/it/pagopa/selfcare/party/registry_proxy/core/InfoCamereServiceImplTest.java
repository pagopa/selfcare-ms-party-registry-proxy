package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.NationalRegistriesConnector;

import java.util.ArrayList;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.Businesses;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class InfoCamereServiceImplTest {
    @Mock
    private NationalRegistriesConnector nationalRegistriesConnector;

    @InjectMocks
    private InfoCamereServiceImpl infoCamereServiceImpl;

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

        assertSame(businesses, infoCamereServiceImpl.institutionsByLegalTaxId("42"));
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

        Businesses response = infoCamereServiceImpl.institutionsByLegalTaxId("42");
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

        Businesses response = infoCamereServiceImpl.institutionsByLegalTaxId("42");
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

        Businesses response = infoCamereServiceImpl.institutionsByLegalTaxId("42");
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

        Businesses response = infoCamereServiceImpl.institutionsByLegalTaxId("42");
        assertEquals("42", response.getLegalTaxId());
        assertEquals(0, response.getBusinessList().size());
        verify(nationalRegistriesConnector).institutionsByLegalTaxId(any());
    }
}

