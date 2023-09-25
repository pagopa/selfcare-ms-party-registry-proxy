package it.pagopa.selfcare.party.registry_proxy.web.model;

import it.pagopa.selfcare.party.registry_proxy.connector.model.PDND;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import lombok.Data;

import java.util.List;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;

@Data
public class DummyPDNDQueryResult implements QueryResult<PDND> {

    private List<PDND> items = List.of(mockInstance(new DummyPDND()));
    private long totalHits;

}