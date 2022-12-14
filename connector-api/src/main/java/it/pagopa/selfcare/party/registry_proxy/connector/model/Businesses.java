package it.pagopa.selfcare.party.registry_proxy.connector.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Businesses {
    @JsonProperty("dataOraEstrazione")
    private String requestDateTime;
    @JsonProperty("cfPersona")
    private String legalTaxId;
    @JsonProperty("elencoImpreseRappresentate")
    private List<Business> businesses;
}
