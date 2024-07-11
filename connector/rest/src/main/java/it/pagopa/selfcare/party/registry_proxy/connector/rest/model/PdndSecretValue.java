package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import lombok.Data;

@Data
public class PdndSecretValue {
    private JwtConfig jwtConfig;
    private String clientId;
    private String secretKey;
}
