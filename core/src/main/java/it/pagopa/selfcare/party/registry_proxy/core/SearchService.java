package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchDocument;

public interface SearchService {
  void indexDocument(SearchDocument document);
}
