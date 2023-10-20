package it.pagopa.selfcare.party.registry_proxy.connector.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class DummyPDND implements Station {

    private String id;
    private String originId;
    private String taxCode;
    private String description;
    private String digitalAddress;
    private boolean anacEnabled;
    private boolean anacEngaged;

}