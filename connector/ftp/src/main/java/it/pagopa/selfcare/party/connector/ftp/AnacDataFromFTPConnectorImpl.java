package it.pagopa.selfcare.party.connector.ftp;

import it.pagopa.selfcare.party.registry_proxy.connector.api.AnacDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.FTPConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
@ConditionalOnProperty(
        value = "file.connector.type",
        havingValue = "sftp")
@PropertySource("classpath:config/ftp-config.properties")
public class AnacDataFromFTPConnectorImpl implements AnacDataConnector {
    private final String sourceFilename;
    private final FTPConnector ftpConnector;

    public AnacDataFromFTPConnectorImpl(@Value("${ftp.anac.filename}") String anacCsvFileName,
                                        FTPConnector ftpConnector) {
        this.ftpConnector = ftpConnector;
        this.sourceFilename = anacCsvFileName;
    }

    @Override
    public InputStream getANACData() {
        log.trace("getStations start");
        return ftpConnector.getFile(sourceFilename);
    }
}
