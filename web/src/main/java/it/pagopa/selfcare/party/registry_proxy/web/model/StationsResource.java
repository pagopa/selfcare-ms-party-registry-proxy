package it.pagopa.selfcare.party.registry_proxy.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class StationsResource {

    @ApiModelProperty(value = "${swagger.model.station.items}", required = true)
    @JsonProperty(required = true)
    @NotNull
    @Valid
    private List<StationResource> items;

    @ApiModelProperty(value = "${swagger.model.station.totalCount}", required = true)
    @JsonProperty(required = true)
    @NotNull
    private Long count;
}
