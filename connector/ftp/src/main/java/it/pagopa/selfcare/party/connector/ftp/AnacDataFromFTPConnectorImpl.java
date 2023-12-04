package it.pagopa.selfcare.party.connector.ftp;

import it.pagopa.selfcare.party.registry_proxy.connector.api.AnacDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.FTPConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
@ConditionalOnProperty(
        value = "file.connector.type",
        havingValue = "sftp")
@PropertySource("classpath:config/ftp-config.properties")
public class AnacDataFromFTPConnectorImpl implements AnacDataConnector {

    private static final String ANAC_DATA_FILE_PREFIX = "collaudo-";
    private static final String ANAC_DATA_FILE_SUFFIX = ".csv";
    private final String directory;
    private final FTPConnector ftpConnector;

    public AnacDataFromFTPConnectorImpl(@Value("${anac.ftp.directory}") String anacDirectory,
                                        FTPConnector ftpConnector) {
        this.ftpConnector = ftpConnector;
        this.directory = anacDirectory;
    }


    @Override
    public Optional<InputStream> getANACData() {
        String fileName = createFileName();
        log.trace("getANACData on filename: {} start", fileName);
        Optional<InputStream> optionalInputStream = ftpConnector.getFile(directory + fileName);
        log.trace("getANACData on filename: {} end", fileName);
        return optionalInputStream;
    }

    private String createFileName() {
        StringBuilder fileNameBuilder = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return fileNameBuilder.append(ANAC_DATA_FILE_PREFIX)
                .append(LocalDateTime.now().format(formatter))
                .append(ANAC_DATA_FILE_SUFFIX)
                .toString();
    }
}
