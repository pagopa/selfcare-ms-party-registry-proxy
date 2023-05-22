package it.pagopa.selfcare.party.registry_proxy.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AOOResource implements AOO {

    private String id;

    @ApiModelProperty(value = "${swagger.model.aoo.codiceIpa}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String codiceIpa;

    @ApiModelProperty(value = "${swagger.model.aoo.denominazioneEnte}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String denominazioneEnte;

    @ApiModelProperty(value = "${swagger.model.aoo.codiceFiscaleEnte}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String codiceFiscaleEnte;

    @ApiModelProperty(value = "${swagger.model.aoo.codiceUniAoo}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String codiceUniAoo;

    @ApiModelProperty(value = "${swagger.model.aoo.denominazioneAoo}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String denominazioneAoo;

    @ApiModelProperty(value = "${swagger.model.aoo.mail1}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String mail1;

    @ApiModelProperty(value = "${swagger.model.aoo.codAoo}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String codAoo;

    @ApiModelProperty(value = "${swagger.model.*.origin}", required = true)
    @JsonProperty(required = true)
    @NotNull
    private Origin origin;
}
