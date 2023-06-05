package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;


public interface UOService {

    QueryResult<UO> findAll(int page, int limit);

    UO findByUnicode(String codiceUniUO);

}
