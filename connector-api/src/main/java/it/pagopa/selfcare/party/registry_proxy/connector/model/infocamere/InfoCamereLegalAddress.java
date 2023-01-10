package it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class InfoCamereLegalAddress {
    @JsonProperty("dataOraEstrazione")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date dateTimeExtraction;

    @JsonProperty("cf")
    private String taxId;

    @JsonProperty("indirizzoLocalizzazione")
    private InfoCamereLocationAddress legalAddress;
}
