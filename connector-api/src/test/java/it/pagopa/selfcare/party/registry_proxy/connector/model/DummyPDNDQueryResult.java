package it.pagopa.selfcare.party.registry_proxy.connector.model;

import lombok.Data;

import java.util.List;

@Data
public class DummyPDNDQueryResult implements QueryResult<PDND> {

    private List<PDND> items = List.of();
    private long totalHits;

}