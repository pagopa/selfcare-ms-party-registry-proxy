package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.FullTextQueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;

import java.util.Optional;

public interface InstitutionService {

    FullTextQueryResult<Institution> search(String searchText, int page, int limit);

    Institution findById(String id, Optional<Origin> origin);

}
