package it.pagopa.selfcare.party.registry_proxy.connector.rest.service;

import io.dapr.client.DaprClient;
import it.pagopa.selfcare.onboarding.crypto.utils.DataEncryptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StorageAsyncService {

    private final DaprClient daprSelcClient;

    public StorageAsyncService(DaprClient daprSelcClient) {
        this.daprSelcClient = daprSelcClient;
    }

    @Async("storageTaskExecutor")
    public void saveStringToStorage(String document, String keyName) {
        try {
            var encryptedDocument = DataEncryptionUtils.encrypt(document);
            var a = daprSelcClient.saveState("blobstorage-state", keyName, encryptedDocument);
            a.block();
            log.info("Document saved into Azure Storage with key: {}", keyName);
        } catch (Exception e) {
            log.error("Impossible to store document with key {} into Azure Storage", keyName, e);
        }
    }
}
