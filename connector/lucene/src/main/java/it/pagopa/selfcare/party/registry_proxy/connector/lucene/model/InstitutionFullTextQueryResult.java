package it.pagopa.selfcare.party.registry_proxy.connector.lucene.model;

import it.pagopa.selfcare.party.registry_proxy.connector.model.FullTextQueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import lombok.Data;

import java.util.List;

@Data
public class InstitutionFullTextQueryResult implements FullTextQueryResult<Institution> {

    private List<Institution> items;
    private long totalHits;

}
