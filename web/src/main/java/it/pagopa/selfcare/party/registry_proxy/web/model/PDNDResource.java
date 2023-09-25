package it.pagopa.selfcare.party.registry_proxy.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.model.PDND;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class PDNDResource implements PDND {

    @ApiModelProperty(value = "${swagger.model.station.id}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String id;

    @ApiModelProperty(value = "${swagger.model.station.originId}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String originId;

    @ApiModelProperty(value = "${swagger.model.station.anacEngaged}")
    private boolean anacEngaged;
    @ApiModelProperty(value = "${swagger.model.station.anacEnabled}")
    private boolean anacEnabled;

    @ApiModelProperty(value = "${swagger.model.station.taxCode}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String taxCode;

    @ApiModelProperty(value = "${swagger.model.station.description}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String description;

    @ApiModelProperty(value = "${swagger.model.station.digitalAddress}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    @Email
    private String digitalAddress;



}
