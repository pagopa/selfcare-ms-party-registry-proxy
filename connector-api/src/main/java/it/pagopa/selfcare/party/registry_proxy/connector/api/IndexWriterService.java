package it.pagopa.selfcare.party.registry_proxy.connector.api;

import java.util.List;

public interface IndexWriterService<T> {

    void adds(List<? extends T> items);

    void deleteAll();

}
