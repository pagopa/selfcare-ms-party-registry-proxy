package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GetBusinessesByLegal;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.InfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.ClientCredentialsResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.TokenType;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.IniPecJwsGenerator;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {InfoCamereConnectorImpl.class})
@ExtendWith(SpringExtension.class)
class InfoCamereConnectorImplTest {
    @Autowired
    private InfoCamereConnectorImpl infoCamereConnectorImpl;

    @MockBean
    private InfoCamereRestClient infoCamereRestClient;

    @MockBean
    private IniPecJwsGenerator iniPecJwsGenerator;

    /**
     * Method under test: {@link InfoCamereConnectorImpl#businessesByLegal(GetBusinessesByLegal)}
     */
    @Test
    void testBusinessesByLegal() {
        ClientCredentialsResponse clientCredentialsResponse = new ClientCredentialsResponse();
        clientCredentialsResponse.setAccessToken("ABC123");
        clientCredentialsResponse.setExpiresIn(1);
        clientCredentialsResponse.setTokenType(TokenType.BEARER);

        Businesses businesses = new Businesses();
        businesses.setBusinesses(new ArrayList<>());
        businesses.setLegalTaxId("42");
        businesses.setRequestDateTime("2020-03-01");
        when(infoCamereRestClient.businessesByLegal((String) any(), (String) any())).thenReturn(businesses);
        when(infoCamereRestClient.getToken((String) any())).thenReturn(clientCredentialsResponse);
        when(iniPecJwsGenerator.createAuthRest()).thenReturn("Create Auth Rest");

        GetBusinessesByLegal getBusinessesByLegal = new GetBusinessesByLegal();
        getBusinessesByLegal.setLegalTaxId("42");
        assertSame(businesses, infoCamereConnectorImpl.businessesByLegal(getBusinessesByLegal));
        verify(infoCamereRestClient).businessesByLegal((String) any(), (String) any());
        verify(infoCamereRestClient).getToken((String) any());
        verify(iniPecJwsGenerator).createAuthRest();
    }
}

