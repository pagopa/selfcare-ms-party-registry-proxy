package it.pagopa.selfcare.party.registry_proxy.web.model;

import lombok.Data;

@Data
public class LocationAddress {
    private String description;
    private String municipality;
    private String province;
    private String address;
    private String zip;
}
