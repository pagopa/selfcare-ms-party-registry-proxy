package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;

import java.util.List;

public interface AnacDataConnector {
    List<Station> getStations();
}
