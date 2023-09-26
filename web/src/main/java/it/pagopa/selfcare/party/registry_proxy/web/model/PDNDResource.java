package it.pagopa.selfcare.party.registry_proxy.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class PDNDResource implements Station {

    @ApiModelProperty(value = "${swagger.model.pdnd.id}", required = true)
    @JsonProperty(required = true)
    private String id;

    @ApiModelProperty(value = "${swagger.model.pdnd.originId}", required = true)
    @JsonProperty(required = true)
    private String originId;

    @ApiModelProperty(value = "${swagger.model.pdnd.anacEngaged}")
    private boolean anacEngaged;
    @ApiModelProperty(value = "${swagger.model.pdnd.anacEnabled}")
    private boolean anacEnabled;

    @ApiModelProperty(value = "${swagger.model.pdnd.taxCode}", required = true)
    @JsonProperty(required = true)
    private String taxCode;

    @ApiModelProperty(value = "${swagger.model.pdnd.description}", required = true)
    @JsonProperty(required = true)
    private String description;

    @ApiModelProperty(value = "${swagger.model.pdnd.digitalAddress}", required = true)
    @JsonProperty(required = true)
    @Email
    private String digitalAddress;

    @ApiModelProperty(value = "${swagger.model.*.origin}", required = true)
    @JsonProperty(required = true)
    private Origin origin;

}
