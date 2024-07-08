package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import lombok.Data;
@Data
public class JwtConfig {
    private String issuer;
    private String subject;
    private String audience;
    private String kid;
    private String purposeId;
}
