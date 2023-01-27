package it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Business {
    @JsonProperty("cfImpresa")
    private String businessTaxId;
    @JsonProperty("denominazione")
    private String businessName;
}
