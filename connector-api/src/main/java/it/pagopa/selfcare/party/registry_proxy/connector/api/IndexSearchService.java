package it.pagopa.selfcare.party.registry_proxy.connector.api;

import java.util.List;

public interface IndexSearchService<T> {

    List<T> searchByText(String searchingField, String searchText, int page, int limit);

}
