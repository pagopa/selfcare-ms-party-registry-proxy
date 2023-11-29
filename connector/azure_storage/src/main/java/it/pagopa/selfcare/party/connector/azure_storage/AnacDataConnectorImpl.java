package it.pagopa.selfcare.party.connector.azure_storage;

import it.pagopa.selfcare.party.registry_proxy.connector.api.AnacDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.FileStorageConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.ResourceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
@ConditionalOnProperty(
        value = "file.connector.type",
        havingValue = "azure",
        matchIfMissing = true)
public class AnacDataConnectorImpl implements AnacDataConnector {

    private final FileStorageConnector fileStorageConnector;
    private final String sourceFilename;

    public AnacDataConnectorImpl(@Value("${blobStorage.anac.filename}") String anacCsvFileName,
                                 FileStorageConnector fileStorageConnector) {
        this.fileStorageConnector = fileStorageConnector;
        this.sourceFilename = anacCsvFileName;
    }

    @Override
    public InputStream getANACData() {
        ResourceResponse resourceResponse;
        try {
            resourceResponse = fileStorageConnector.getFile(sourceFilename);
            return new ByteArrayInputStream(resourceResponse.getData());
        } catch (Exception e) {
            log.error("Impossible to retrieve file ANAC. Error: {}", e.getMessage(), e);
            return InputStream.nullInputStream();
        }
    }
}
