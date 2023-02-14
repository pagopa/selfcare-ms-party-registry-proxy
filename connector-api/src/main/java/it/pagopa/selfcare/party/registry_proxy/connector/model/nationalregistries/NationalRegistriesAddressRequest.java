package it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NationalRegistriesAddressRequest {

    @JsonProperty("filter")
    private NationalRegistriesAddressFilter filter;
}
