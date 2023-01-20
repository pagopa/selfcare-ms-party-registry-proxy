package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
}

