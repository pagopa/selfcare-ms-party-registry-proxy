package it.pagopa.selfcare.party.registry_proxy.connector.api;

import java.io.InputStream;
import java.util.Optional;

public interface AnacDataConnector {
    Optional<InputStream> getANACData();
}
