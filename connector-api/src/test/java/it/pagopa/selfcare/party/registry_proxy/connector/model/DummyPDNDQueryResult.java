package it.pagopa.selfcare.party.registry_proxy.connector.model;

import lombok.Data;

import java.util.List;

@Data
public class DummyPDNDQueryResult implements QueryResult<Station> {

    private List<Station> items = List.of();
    private long totalHits;

}