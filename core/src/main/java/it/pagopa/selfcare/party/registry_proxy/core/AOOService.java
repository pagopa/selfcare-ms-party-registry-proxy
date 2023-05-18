package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;

import java.util.Optional;

public interface AOOService {

    QueryResult<AOO> search(Optional<Origin> origin, int page, int limit);

    AOO findById(String id, Origin origin);

}
