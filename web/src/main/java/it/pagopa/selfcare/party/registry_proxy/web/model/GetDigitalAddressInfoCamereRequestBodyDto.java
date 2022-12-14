package it.pagopa.selfcare.party.registry_proxy.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetDigitalAddressInfoCamereRequestBodyDto {

    @JsonProperty("filter")
    private GetDigitalAddressInfoCamereRequestBodyFilterDto filter;

}
