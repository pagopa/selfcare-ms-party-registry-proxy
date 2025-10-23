package it.pagopa.selfcare.party.registry_proxy.connector.rest.service;

import feign.FeignException;
import it.pagopa.selfcare.onboarding.crypto.utils.DataEncryptionUtils;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDVisuraInfoCamereRawRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDVisuraInfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.config.PDNDVisuraInfoCamereRestClientConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.ClientCredentialsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class PdndService {

    @Autowired
    TokenProviderVisura tokenProviderVisura;

    @Autowired
    PDNDVisuraInfoCamereRawRestClient pdndVisuraInfoCamereRawRestClient;

    @Autowired
    PDNDVisuraInfoCamereRestClientConfig pdndVisuraInfoCamereRestClientConfig;

    private static final String BEARER = "Bearer ";


    @Cacheable(value = "visure", key = "#encryptedTaxCode")
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

}
