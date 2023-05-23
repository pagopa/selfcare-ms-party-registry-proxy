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
    private String mail1;

    @ApiModelProperty(value = "${swagger.model.*.origin}", required = true)
    @JsonProperty(required = true)
    @NotNull
    private Origin origin;

    @ApiModelProperty(value = "${swagger.model.uo.dataInstituzione}", required = true)
    private String dataIstituzione;

    @ApiModelProperty(value = "${swagger.model.uo.nomeResponsabile}", required = true)
    private String nomeResponsabile;

    @ApiModelProperty(value = "${swagger.model.uo.cognomeResponsabile}", required = true)
    private String cognomeResponsabile;

    @ApiModelProperty(value = "${swagger.model.uo.mailResponsabile}", required = true)
    private String mailResponsabile;

    @ApiModelProperty(value = "${swagger.model.uo.telefonoResponsabile}", required = true)
    private String telefonoResponsabile;

    @ApiModelProperty(value = "${swagger.model.uo.codiceComuneISTAT}", required = true)
    private String codiceComuneISTAT;

    @ApiModelProperty(value = "${swagger.model.uo.codiceCatastaleComune}", required = true)
    private String codiceCatastaleComune;

    @ApiModelProperty(value = "${swagger.model.uo.CAP}", required = true)
    private String CAP;

    @ApiModelProperty(value = "${swagger.model.uo.indirizzo}", required = true)
    private String indirizzo;

    @ApiModelProperty(value = "${swagger.model.uo.telefono}", required = true)
    private String telefono;

    @ApiModelProperty(value = "${swagger.model.uo.fax}", required = true)
    private String fax;

    @ApiModelProperty(value = "${swagger.model.uo.tipoMail1}", required = true)
    private String tipoMail1;

    @ApiModelProperty(value = "${swagger.model.uo.url}", required = true)
    private String url;

    @ApiModelProperty(value = "${swagger.model.uo.dataAggiornamento}", required = true)
    private String dataAggiornamento;
}
