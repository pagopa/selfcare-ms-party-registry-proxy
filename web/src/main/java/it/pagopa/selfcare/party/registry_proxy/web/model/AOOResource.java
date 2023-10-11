package it.pagopa.selfcare.party.registry_proxy.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AOOResource implements AOO {

    private String id;

    @ApiModelProperty(required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String codiceIpa;

    @ApiModelProperty(required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String denominazioneEnte;

    @ApiModelProperty(required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String codiceFiscaleEnte;

    @ApiModelProperty(value = "${swagger.model.aoo.codiceUniAoo}", required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String codiceUniAoo;

    @ApiModelProperty(required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String denominazioneAoo;

    @ApiModelProperty(required = true)
    private String dataIstituzione;

    @ApiModelProperty(required = true)
    private String nomeResponsabile;

    @ApiModelProperty(required = true)
    private String cognomeResponsabile;

    @ApiModelProperty(required = true)
    private String mailResponsabile;

    @ApiModelProperty(required = true)
    private String telefonoResponsabile;

    @ApiModelProperty(required = true)
    private String codiceComuneISTAT;

    @ApiModelProperty(required = true)
    private String codiceCatastaleComune;

    @ApiModelProperty(required = true)
    private String CAP;

    @ApiModelProperty(required = true)
    private String indirizzo;

    @ApiModelProperty(required = true)
    private String telefono;

    @ApiModelProperty(required = true)
    private String fax;

    private String tipoMail1;

    @ApiModelProperty(required = true)
    private String protocolloInformatico;

    @ApiModelProperty(required = true)
    private String URIProtocolloInformatico;

    @ApiModelProperty(required = true)
    private String dataAggiornamento;

    private String mail1;

    @ApiModelProperty(required = true)
    @JsonProperty(required = true)
    @NotBlank
    private String codAoo;

    @ApiModelProperty(required = true)
    @JsonProperty(required = true)
    @NotNull
    private Origin origin;
}
