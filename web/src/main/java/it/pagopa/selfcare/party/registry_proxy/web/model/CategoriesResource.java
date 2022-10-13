package it.pagopa.selfcare.party.registry_proxy.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CategoriesResource {

    @ApiModelProperty(value = "${swagger.model.categories.items}", required = true)
    @JsonProperty(required = true)
    @NotNull
    @Valid
    private List<CategoryResource> items;

}
