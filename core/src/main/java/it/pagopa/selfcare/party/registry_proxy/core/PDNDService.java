package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.PDND;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;

import java.util.Optional;

public interface PDNDService {
    QueryResult<PDND> search(Optional<String> searchText, int page, int limit);

    PDND findByTaxId(String taxId);
}
