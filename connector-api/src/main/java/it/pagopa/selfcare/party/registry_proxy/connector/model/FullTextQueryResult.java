package it.pagopa.selfcare.party.registry_proxy.connector.model;

import java.util.List;

public interface FullTextQueryResult<T> {

    List<T> getItems();

    long getTotalHits();

}
