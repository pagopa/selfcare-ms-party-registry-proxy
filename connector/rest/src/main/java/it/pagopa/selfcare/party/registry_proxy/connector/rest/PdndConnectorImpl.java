package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.commons.base.logging.LogUtils;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PdndRestClient;
import it.pagopa.selfcare.party.user_registry.generated.openapi.v1.dto.PdndClientCredentialsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.Const.PDND_TOKEN_CACHE;

@Slf4j
@Service
public class PdndConnectorImpl {
    private final PdndRestClient pdndRestClient;

    public PdndConnectorImpl(PdndRestClient pdndRestClient) {
        this.pdndRestClient = pdndRestClient;
    }

    @Cacheable(value = PDND_TOKEN_CACHE, key = "#clientId", cacheManager = PDND_TOKEN_CACHE)
    public PdndClientCredentialsResponse createToken(String clientAssertion, String clientAssertionType, String grantType, String clientId) {
        log.debug(LogUtils.CONFIDENTIAL_MARKER, " clientAssertionType = {}, grantType = {}, clientId = {}", clientAssertionType, grantType, clientId);
        ResponseEntity<PdndClientCredentialsResponse> result = pdndRestClient._createToken(clientAssertion, clientAssertionType, grantType, clientId);
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "PdndClientCredentialsResponse result = {}", result.getBody());
        return result.getBody();
    }

}