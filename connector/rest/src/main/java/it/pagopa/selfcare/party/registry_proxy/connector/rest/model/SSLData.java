package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import lombok.Data;

@Data
public class SSLData {
    private String cert;
    private String key;
    private String pub;
    private String trust;
}
