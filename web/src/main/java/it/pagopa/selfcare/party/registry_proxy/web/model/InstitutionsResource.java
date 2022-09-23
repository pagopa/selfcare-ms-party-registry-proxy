package it.pagopa.selfcare.party.registry_proxy.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class InstitutionsResource {

    @ApiModelProperty(value = "${swagger.model.institutions.items}", required = true)
    @JsonProperty(required = true)
    @Valid
    private List<InstitutionResource> items;

    @ApiModelProperty(value = "${swagger.model.institutions.totalCount}", required = true)
    @JsonProperty(required = true)
    private long totalCount;

}
