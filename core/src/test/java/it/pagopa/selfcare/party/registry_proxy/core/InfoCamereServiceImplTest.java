package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;

import java.util.ArrayList;

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
    private InfoCamereConnector infoCamereConnector;

    @InjectMocks
    private InfoCamereServiceImpl infoCamereServiceImpl;

    /**
     * Method under test: {@link InfoCamereServiceImpl#institutionsByLegalTaxId(String)}
     */
    @Test
    void testInstitutionsByLegalTaxId() {
        Businesses businesses = new Businesses();
        businesses.setBusinesses(new ArrayList<>());
        businesses.setLegalTaxId("42");
        businesses.setRequestDateTime("2020-03-01");
        when(infoCamereConnector.institutionsByLegalTaxId(any())).thenReturn(businesses);

        assertSame(businesses, infoCamereServiceImpl.institutionsByLegalTaxId("42"));
        verify(infoCamereConnector).institutionsByLegalTaxId(any());
    }

    /**
     * Method under test: {@link InfoCamereServiceImpl#institutionsByLegalTaxId(String)}
     */
    @Test
    void testInstitutionsByLegalTaxIdNotFound() {
        Businesses businesses = new Businesses();
        businesses.setCode("WSPA_ERR_04");
        businesses.setDescription("LR Not found");
        businesses.setAppName("wspa-lrpf");
        businesses.setTimestamp("2023-03-10T15:40:01.598877+01:00");
        when(infoCamereConnector.institutionsByLegalTaxId(any())).thenReturn(businesses);

        Businesses response = infoCamereServiceImpl.institutionsByLegalTaxId("42");
        assertEquals("42", response.getLegalTaxId());
        assertEquals(0, response.getBusinesses().size());
        verify(infoCamereConnector).institutionsByLegalTaxId(any());
    }
}

