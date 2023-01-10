package it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InfoCamereLocationAddress {
    @JsonProperty("denominazione")
    private String address;

    @JsonProperty("comune")
    private String municipality;

    @JsonProperty("provincia")
    private String province;

    @JsonProperty("toponimo")
    private String toponym;

    @JsonProperty("via")
    private String street;

    @JsonProperty("nCivico")
    private String streetNumber;

    @JsonProperty("cap")
    private String postalCode;
}
