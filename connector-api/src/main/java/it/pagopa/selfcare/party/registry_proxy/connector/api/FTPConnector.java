package it.pagopa.selfcare.party.registry_proxy.connector.api;


import java.io.InputStream;

public interface FTPConnector {

    InputStream getFile(String sourceFilename);
}
