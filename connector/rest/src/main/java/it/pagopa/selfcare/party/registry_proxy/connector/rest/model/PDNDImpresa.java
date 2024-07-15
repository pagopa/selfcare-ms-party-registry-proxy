package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PDNDImpresa {

    @JsonProperty("ProgressivoImpresa")
    private long progressivoImpresa;

    @JsonProperty("CodiceFiscale")
    private String businessTaxId;

    @JsonProperty("Denominazione")
    private String businessName;

    @JsonProperty("NaturaGiuridica")
    private String legalNature;

    @JsonProperty("DescNaturaGiuridica")
    private String legalNatureDescription;

    @JsonProperty("Cciaa")
    private String cciaa;

    @JsonProperty("NRea")
    private String nRea;

    @JsonProperty("StatoImpresa")
    private String businessStatus;

    @JsonProperty("IndirizzoSedeLegale")
    private PDNDSedeImpresa businessAddress;

    @JsonProperty("PEC")
    private String digitalAddress;


    public String getAddress() {
        return businessAddress.getToponimoSede() + " " + businessAddress.getViaSede() + " " + businessAddress.getNcivicoSede();
    }

}
