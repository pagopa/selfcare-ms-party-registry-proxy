package it.pagopa.selfcare.party.registry_proxy.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UOsResource {

    @ApiModelProperty(value = "${swagger.model.uos.items}", required = true)
    @JsonProperty(required = true)
    @NotNull
    @Valid
    private List<UOResource> items;

    @ApiModelProperty(value = "${swagger.model.uos.totalCount}", required = true)
    @JsonProperty(required = true)
    @NotNull
    private Long count;

}
