package it.pagopa.selfcare.party.registry_proxy.connector.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Business {
    @JsonProperty("cfImpresa")
    private String taxId;
    @JsonProperty("denominazione")
    private String businessName;
}
