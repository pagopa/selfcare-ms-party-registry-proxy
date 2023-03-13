package it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere;

import lombok.Data;

@Data
public class InfoCamereCommonError {
    private String code;
    private String appName;
    private String description;
    private String timestamp;
}
