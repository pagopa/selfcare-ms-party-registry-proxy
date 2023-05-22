package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;


public interface AOOService {

    QueryResult<AOO> findAll(int page, int limit);

    AOO findByUnicode(String codiceUniAOO);

}
