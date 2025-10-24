package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PdndRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.ClientCredentialsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdndConnectorImplTest {
    @InjectMocks
    PdndConnectorImpl pdndConnectorImpl;
    @Mock
    PdndRestClient pdndRestClient;

    @Test
    void createTokenReturnsValidResponseForValidCredentials() {
        // Given
        String clientAssertion = "validAssertion";
        String clientAssertionType = "validType";
        String grantType = "client_credentials";
        String clientId = "validClientId";
        ClientCredentialsResponse expectedResponse = new ClientCredentialsResponse();
        expectedResponse.setAccessToken("validAccessToken");
        when(pdndRestClient.createToken(any())).thenReturn(expectedResponse);

        // When
        ClientCredentialsResponse actualResponse = pdndConnectorImpl.createToken(clientAssertion, clientAssertionType, grantType, clientId);

        // Then
        assertNotNull(actualResponse);
        assertEquals("validAccessToken", actualResponse.getAccessToken());
    }

}