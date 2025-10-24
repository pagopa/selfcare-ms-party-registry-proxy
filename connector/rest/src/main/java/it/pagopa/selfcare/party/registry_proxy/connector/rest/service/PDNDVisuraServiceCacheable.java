package it.pagopa.selfcare.party.registry_proxy.connector.rest.service;

import feign.FeignException;
import it.pagopa.selfcare.onboarding.crypto.utils.DataEncryptionUtils;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDInfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDVisuraInfoCamereRawRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.PDNDInfoCamereRestClientConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.PDNDVisuraInfoCamereRestClientConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.ClientCredentialsResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PDNDImpresa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class PDNDVisuraServiceCacheable {

    private final TokenProviderVisura tokenProviderVisura;
    private final TokenProvider tokenProviderPDND;
    private final PDNDVisuraInfoCamereRawRestClient pdndVisuraInfoCamereRawRestClient;
    private final PDNDVisuraInfoCamereRestClientConfig pdndVisuraInfoCamereRestClientConfig;
    private final PDNDInfoCamereRestClientConfig pdndInfoCamereRestClientConfig;
    private final PDNDInfoCamereRestClient pdndInfoCamereRestClient;

    private static final String BEARER = "Bearer ";

    public PDNDVisuraServiceCacheable(TokenProviderVisura tokenProviderVisura,
                                      PDNDVisuraInfoCamereRawRestClient pdndVisuraInfoCamereRawRestClient,
                                      PDNDVisuraInfoCamereRestClientConfig pdndVisuraInfoCamereRestClientConfig,
                                      PDNDInfoCamereRestClient pdndInfoCamereRestClient,
                                      TokenProvider tokenProvider,
                                      PDNDInfoCamereRestClientConfig pdndInfoCamereRestClientConfig) {
        this.tokenProviderVisura = tokenProviderVisura;
        this.pdndVisuraInfoCamereRawRestClient = pdndVisuraInfoCamereRawRestClient;
        this.pdndVisuraInfoCamereRestClientConfig = pdndVisuraInfoCamereRestClientConfig;
        this.pdndInfoCamereRestClient = pdndInfoCamereRestClient;
        this.tokenProviderPDND = tokenProvider;
        this.pdndInfoCamereRestClientConfig = pdndInfoCamereRestClientConfig;
    }

    @Caching(cacheable = {
            @Cacheable(cacheManager = "visureRedisCacheManagerL2", key = "#encryptedTaxCode", cacheNames = "visure"),
            @Cacheable(cacheManager = "visureRedisCacheManagerL1", key = "#encryptedTaxCode", cacheNames = "visure")
    })
    public String getEncryptedDocument(String encryptedTaxCode) {
        log.info("getEncryptedDocument for {} START", encryptedTaxCode);
        ClientCredentialsResponse tokenResponse = tokenProviderVisura.getTokenPdnd(pdndVisuraInfoCamereRestClientConfig.getPdndSecretValue());
        String bearer = BEARER + tokenResponse.getAccessToken();
        var taxCode = DataEncryptionUtils.decrypt(encryptedTaxCode);
        try {
            byte[] document = pdndVisuraInfoCamereRawRestClient.getRawInstitutionDetail(taxCode, bearer);
            log.info("getEncryptedDocument for {} END", encryptedTaxCode);
            return DataEncryptionUtils.encrypt(new String(document, StandardCharsets.UTF_8));
        } catch (FeignException e) {
            if (e instanceof FeignException.BadRequest) {
                throw new ResourceNotFoundException("No institution found for taxCode: " + taxCode);
            }
            log.error("FeignException occurred while retrieving institution detail", e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected exception occurred while retrieving institution detail", e);
            throw new IllegalArgumentException("Unexpected error while retrieving institution detail", e);
        }
    }

    @Caching(cacheable = {
            @Cacheable(cacheManager = "visureRedisCacheManagerL2", key = "#encryptedTaxCode", cacheNames = "imprese"),
            @Cacheable(cacheManager = "visureRedisCacheManagerL1", key = "#encryptedTaxCode", cacheNames = "imprese")
    })
    public PDNDImpresa getInfocamereImpresa(String taxCode) {
        ClientCredentialsResponse tokenResponse = tokenProviderPDND.getTokenPdnd(pdndInfoCamereRestClientConfig.getPdndSecretValue());
        String bearer = BEARER + tokenResponse.getAccessToken();
        return pdndInfoCamereRestClient.retrieveInstitutionPdndByTaxCode(taxCode, bearer).get(0);
    }

}
