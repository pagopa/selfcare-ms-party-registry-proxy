package it.pagopa.selfcare.party.registry_proxy.connector.rest.service;

import it.pagopa.selfcare.party.registry_proxy.connector.rest.PdndConnectorImpl;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.JwtConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PdndSecretValue;
import it.pagopa.selfcare.party.user_registry.generated.openapi.v1.dto.PdndClientCredentialsResponse;
import it.pagopa.selfcare.party.user_registry.generated.openapi.v1.dto.TokenType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TokenProviderTest {

    @InjectMocks
    TokenProvider tokenProvider;
    @Mock
    AssertionGenerator assertionGenerator;
    @Mock
    PdndConnectorImpl pdndClient;

    @Test
    void getTokenPdndSuccess() {
        // given
        PdndSecretValue pdndSecretValue = new PdndSecretValue();
        pdndSecretValue.setClientId("clientId");
        pdndSecretValue.setSecretKey("secretKey");
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setAudience("audience");
        jwtConfig.setIssuer("issuer");
        jwtConfig.setKid("kid");
        jwtConfig.setPurposeId("purposeId");
        jwtConfig.setSubject("subject");
        pdndSecretValue.setJwtConfig(jwtConfig);

        PdndClientCredentialsResponse response = new PdndClientCredentialsResponse();
        response.setAccessToken("accessToken");
        response.setExpiresIn(3600);
        response.setTokenType(TokenType.BEARER);

        // when
        when(assertionGenerator.generateClientAssertion(jwtConfig, pdndSecretValue.getSecretKey()))
                .thenReturn("clientAssertion");
        when(pdndClient.createToken(any(), any(), any(), any()))
                .thenReturn(response);

        PdndClientCredentialsResponse token = tokenProvider.getTokenPdnd(pdndSecretValue);
        // then
        assertEquals("accessToken", token.getAccessToken());
        assertEquals(3600, token.getExpiresIn());
        assertEquals(TokenType.BEARER, token.getTokenType());
        verify(assertionGenerator, times(1))
                .generateClientAssertion(jwtConfig, pdndSecretValue.getSecretKey());
        verifyNoMoreInteractions(assertionGenerator);
    }
}