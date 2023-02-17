package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdELegalOKDto {

    @JsonProperty("verificationResult")
    private Boolean verificationResult;

    @JsonProperty("resultDetail")
    private AdEResultDetailEnum resultDetail;

    @JsonProperty("resultCode")
    private AdEResultCodeEnum resultCode;
}
