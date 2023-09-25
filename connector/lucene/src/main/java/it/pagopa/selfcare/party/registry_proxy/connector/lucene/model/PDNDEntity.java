package it.pagopa.selfcare.party.registry_proxy.connector.lucene.model;

import it.pagopa.selfcare.party.registry_proxy.connector.model.PDND;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class PDNDEntity implements PDND {

    private String id;
    private String originId;
    private boolean anacEnabled;
    private boolean anacEngaged;
    private String taxCode;
    private String description;
    private String digitalAddress;

}
