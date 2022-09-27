package it.pagopa.selfcare.party.registry_proxy.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class InstitutionResource implements Institution {

    @ApiModelProperty(value = "${swagger.model.institution.id}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String id;

    @ApiModelProperty(value = "${swagger.model.institution.originId}", required = true)
    @JsonProperty(required = true)
    @NotBlank
//    @Pattern(regexp = "^[a-z]{1,12}$")
    private String originId;

    @ApiModelProperty(value = "${swagger.model.institution.o}")
//    @Pattern(regexp = "^[a-z]{1,12}$")
    private String o;

    @ApiModelProperty(value = "${swagger.model.institution.ou}")
//    @Pattern(regexp = "^[a-z]{1,12}$")
    private String ou;

    @ApiModelProperty(value = "${swagger.model.institution.aoo}")
//    @Pattern(regexp = "^[a-z]{1,12}$")
    private String aoo;

    @ApiModelProperty(value = "${swagger.model.institution.taxCode}", required = true)
    @JsonProperty(required = true)
    @NotBlank
//    @Pattern(regexp = "[\\d]{10,13}")
    private String taxCode;

    @ApiModelProperty(value = "${swagger.model.institution.category}")
//    @Pattern(regexp = "[a-zA-Z\\d]{1,12}")
    private String category;

    @ApiModelProperty(value = "${swagger.model.institution.description}", required = true)
    @JsonProperty(required = true)
    @NotBlank
//    @Pattern(regexp = "^[A-Za-z èàòùìÈÀÒÙÌ]{2,30}$")
    private String description;

    @ApiModelProperty(value = "${swagger.model.institution.digitalAddress}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    @Email
    private String digitalAddress;

    @ApiModelProperty(value = "${swagger.model.institution.address}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String address;

    @ApiModelProperty(value = "${swagger.model.institution.zipCode}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String zipCode;

    @ApiModelProperty(value = "${swagger.model.institution.origin}", required = true)
    @JsonProperty(required = true)
    @NotNull
    private Origin origin;

}
