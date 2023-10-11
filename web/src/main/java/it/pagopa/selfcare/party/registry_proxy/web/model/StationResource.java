package it.pagopa.selfcare.party.registry_proxy.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class StationResource implements Station {

    @ApiModelProperty(value = "${swagger.model.station.id}", required = true)
    @JsonProperty(required = true)
    private String id;

    @ApiModelProperty(value = "${swagger.model.station.originId}", required = true)
    @JsonProperty(required = true)
    private String originId;

    private boolean anacEngaged;
    
    private boolean anacEnabled;

    @ApiModelProperty(value = "${swagger.model.station.taxCode}", required = true)
    @JsonProperty(required = true)
    private String taxCode;

    @ApiModelProperty(value = "${swagger.model.station.description}", required = true)
    @JsonProperty(required = true)
    private String description;

    @ApiModelProperty(value = "${swagger.model.station.digitalAddress}", required = true)
    @JsonProperty(required = true)
    @Email
    private String digitalAddress;

    @ApiModelProperty(value = "${swagger.model.*.origin}", required = true)
    @JsonProperty(required = true)
    private Origin origin;

}
