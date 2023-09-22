package it.pagopa.selfcare.party.registry_proxy.web.model;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.PDND;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class DummyPDND implements PDND {

    private String id;
    private String originId;
    private String taxCode;
    private String description;
    private String digitalAddress;
    private boolean anacEnabled;
    private boolean anacEngaged;
    private Origin origin;

}