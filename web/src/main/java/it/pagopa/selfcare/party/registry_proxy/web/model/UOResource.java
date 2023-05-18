package it.pagopa.selfcare.party.registry_proxy.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UOResource implements UO {

    private String id;

    @ApiModelProperty(value = "${swagger.model.uo.codiceIpa}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String codiceIpa;

    @ApiModelProperty(value = "${swagger.model.uo.denominazioneEnte}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String denominazioneEnte;

    @ApiModelProperty(value = "${swagger.model.uo.codiceFiscaleEnte}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String codiceFiscaleEnte;

    @ApiModelProperty(value = "${swagger.model.uo.codiceUniUo}")
    @JsonProperty(required = true)
    @NotBlank
    private String codiceUniUo;

    @ApiModelProperty(value = "${swagger.model.uo.codiceUniUoPadre}")
    private String codiceUniUoPadre;

    @ApiModelProperty(value = "${swagger.model.uo.codiceUniAoo}", required = true)
    private String codiceUniAoo;

    @ApiModelProperty(value = "${swagger.model.uo.descrizioneUo}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String descrizioneUo;

    @ApiModelProperty(value = "${swagger.model.uo.mail1}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String mail1;

    @ApiModelProperty(value = "${swagger.model.*.origin}", required = true)
    @JsonProperty(required = true)
    @NotNull
    private Origin origin;
}
