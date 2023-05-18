package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;

import java.util.Optional;

public interface UOService {

    QueryResult<UO> search(Optional<Origin> origin, int page, int limit);

    UO findById(String id, Origin origin);

}
