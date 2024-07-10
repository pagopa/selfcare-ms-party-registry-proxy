package it.pagopa.selfcare.party.registry_proxy.connector.rest.service;

import it.pagopa.selfcare.party.registry_proxy.connector.rest.PdndConnectorImpl;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PdndSecretValue;
import it.pagopa.selfcare.party.user_registry.generated.openapi.v1.dto.PdndClientCredentialsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenProvider {

    private final String clientAssertionType;
    private final String grantType;
    private final AssertionGenerator assertionGenerator;
    private final PdndConnectorImpl pdndClient;
    public TokenProvider(AssertionGenerator assertionGenerator,
                         PdndConnectorImpl pdndClient,
                         @Value("${rest-client.pdnd.client-assertion-type}") String clientAssertionType,
                         @Value("${rest-client.pdnd.grant-type}") String grantType) {
        this.assertionGenerator = assertionGenerator;
        this.clientAssertionType = clientAssertionType;
        this.grantType = grantType;
        this.pdndClient = pdndClient;
    }

    public PdndClientCredentialsResponse getTokenPdnd(PdndSecretValue pdndSecretValue) {
        log.info("START - TokenProvider.getTokenPdnd");
        String clientAssertion = assertionGenerator.generateClientAssertion(pdndSecretValue.getJwtConfig(), pdndSecretValue.getSecretKey());
        return pdndClient.createToken(clientAssertion, clientAssertionType, grantType, pdndSecretValue.getClientId());
    }

}
