package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.FullTextQueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;

import java.util.List;

public interface IndexSearchService<T> {

    FullTextQueryResult<T> fullTextSearch(Institution.Field searchingField, String searchText, int page, int limit);

    List<T> search(Institution.Field searchingField, String searchingValue);

}
