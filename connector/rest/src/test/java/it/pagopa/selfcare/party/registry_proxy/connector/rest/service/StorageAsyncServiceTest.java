package it.pagopa.selfcare.party.registry_proxy.connector.rest.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.dapr.client.DaprClient;
import it.pagopa.selfcare.onboarding.crypto.utils.DataEncryptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Mono;

@SpringJUnitConfig
@EnableAsync
@TestConfiguration
class StorageAsyncServiceTest {

    @Mock
    private DaprClient daprSelcClient;

    @InjectMocks
    private StorageAsyncService storageAsyncService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storageAsyncService = new StorageAsyncService(daprSelcClient);
    }

    @Test
    void testSaveStringToStorageSuccess() throws InterruptedException {
        final String document = "<xml>test</xml>";
        final String keyName = "visura_ABC_2025.xml";

        when(daprSelcClient.saveState(anyString(), anyString(), any()))
                .thenReturn(Mono.empty());

        try (MockedStatic<DataEncryptionUtils> encryptionMock = mockStatic(DataEncryptionUtils.class)) {
            encryptionMock.when(() -> DataEncryptionUtils.encrypt(document))
                    .thenReturn("encrypted_data");

            storageAsyncService.saveStringToStorage(document, keyName);

            Thread.sleep(1000);

            verify(daprSelcClient, times(1))
                    .saveState(eq("blobstorage-state"), eq(keyName), eq("encrypted_data"));
        }
    }

    @Test
    void testSaveStringToStorageError() {
        final String document = "<xml>broken</xml>";
        final String keyName = "visura_ERR_2025.xml";

        try (MockedStatic<DataEncryptionUtils> encryptionMock = mockStatic(DataEncryptionUtils.class)) {
            encryptionMock.when(() -> DataEncryptionUtils.encrypt(document))
                    .thenThrow(new RuntimeException("Encryption failed"));

            storageAsyncService.saveStringToStorage(document, keyName);

            verify(daprSelcClient, never()).saveState(any(), any(), any());
            encryptionMock.verify(() -> DataEncryptionUtils.encrypt(document), times(1));
        }
    }
}
