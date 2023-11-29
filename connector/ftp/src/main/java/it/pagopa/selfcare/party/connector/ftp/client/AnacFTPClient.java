package it.pagopa.selfcare.party.connector.ftp.client;

import it.pagopa.selfcare.party.registry_proxy.connector.api.FTPConnector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

@Component
@Slf4j
@PropertySource("classpath:config/ftp-config.properties")
public class AnacFTPClient implements FTPConnector {

    private final FTPClient ftpClient;
    public AnacFTPClient(@Value("${ftp.external.ip}") String address,
                         @Value("${ftp.user}")String user,
                         @Value("${ftp.password}")String password) {

        FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
        conf.setServerLanguageCode("it");

        ftpClient = new FTPClient();
        ftpClient.configure(conf);

        try {
            InetAddress server = InetAddress.getByName(address);
            ftpClient.connect(server);
            ftpClient.login(user, password);
        } catch (IOException e) {
            log.error("Error during sftp connection. Error: {}", e.getMessage(), e);
        }
    }

    @Override
    public InputStream getFile(String sourceFilename) {
        try {
            return ftpClient.retrieveFileStream(sourceFilename);
        } catch (IOException e) {
            log.error("Impossible to retrieve file ANAC. Error: {}", e.getMessage(), e);
            return InputStream.nullInputStream();
        }
    }
}