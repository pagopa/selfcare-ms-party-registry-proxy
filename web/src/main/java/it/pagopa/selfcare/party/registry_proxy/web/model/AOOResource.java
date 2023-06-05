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

    @ApiModelProperty(value = "${swagger.model.aoo.dataInstituzione}", required = true)
    private String dataIstituzione;

    @ApiModelProperty(value = "${swagger.model.aoo.nomeResponsabile}", required = true)
    private String nomeResponsabile;

    @ApiModelProperty(value = "${swagger.model.aoo.cognomeResponsabile}", required = true)
    private String cognomeResponsabile;

    @ApiModelProperty(value = "${swagger.model.aoo.mailResponsabile}", required = true)
    private String mailResponsabile;

    @ApiModelProperty(value = "${swagger.model.aoo.telefonoResponsabile}", required = true)
    private String telefonoResponsabile;

    @ApiModelProperty(value = "${swagger.model.aoo.codiceComuneISTAT}", required = true)
    private String codiceComuneISTAT;

    @ApiModelProperty(value = "${swagger.model.aoo.codiceCatastaleComune}", required = true)
    private String codiceCatastaleComune;

    @ApiModelProperty(value = "${swagger.model.aoo.CAP}", required = true)
    private String CAP;

    @ApiModelProperty(value = "${swagger.model.aoo.indirizzo}", required = true)
    private String indirizzo;

    @ApiModelProperty(value = "${swagger.model.aoo.telefono}", required = true)
    private String telefono;

    @ApiModelProperty(value = "${swagger.model.aoo.fax}", required = true)
    private String fax;

    @ApiModelProperty(value = "${swagger.model.aoo.tipoMail1}", required = true)
    private String tipoMail1;

    @ApiModelProperty(value = "${swagger.model.aoo.protocolloInformatico}", required = true)
    private String protocolloInformatico;

    @ApiModelProperty(value = "${swagger.model.aoo.URIProtocolloInformatico}", required = true)
    private String URIProtocolloInformatico;

    @ApiModelProperty(value = "${swagger.model.aoo.dataAggiornamento}", required = true)
    private String dataAggiornamento;

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
