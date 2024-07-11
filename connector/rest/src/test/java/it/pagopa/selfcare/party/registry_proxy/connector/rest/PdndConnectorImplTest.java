package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.rest.cache.CacheConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PdndRestClient;
import it.pagopa.selfcare.party.user_registry.generated.openapi.v1.dto.PdndClientCredentialsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = {CacheConfig.class})
@TestPropertySource(locations = "classpath:config/pdnd-rest-client.properties")
@EnableCaching
class PdndConnectorImplTest {
    @InjectMocks
    static PdndConnectorImpl pdndConnectorImpl;
    @Mock
    static PdndRestClient pdndRestClient;
    @Test
    void createTokenReturnsValidResponseForValidCredentials() {
        // Given
        String clientAssertion = "validAssertion";
        String clientAssertionType = "validType";
        String grantType = "client_credentials";
        String clientId = "validClientId";
        PdndClientCredentialsResponse expectedResponse = new PdndClientCredentialsResponse();
        expectedResponse.setAccessToken("validAccessToken");
        ResponseEntity<PdndClientCredentialsResponse> responseEntity = ResponseEntity.ok(expectedResponse);
        when(pdndRestClient._createToken(clientAssertion, clientAssertionType, grantType, clientId)).thenReturn(responseEntity);

        // When
        PdndClientCredentialsResponse actualResponse = pdndConnectorImpl.createToken(clientAssertion, clientAssertionType, grantType, clientId);

        // Then
        assertNotNull(actualResponse);
        assertEquals("validAccessToken", actualResponse.getAccessToken());
    }

    @Test
    void createTokenThrowsExceptionForInvalidCredentials() {
        // Given
        String clientAssertion = "invalidAssertion";
        String clientAssertionType = "invalidType";
        String grantType = "client_credentials";
        String clientId = "invalidClientId";
        when(pdndRestClient._createToken(clientAssertion, clientAssertionType, grantType, clientId)).thenThrow(new RuntimeException("Invalid credentials"));

        // When & Then
        assertThrows(RuntimeException.class, () -> pdndConnectorImpl.createToken(clientAssertion, clientAssertionType, grantType, clientId));
    }

}