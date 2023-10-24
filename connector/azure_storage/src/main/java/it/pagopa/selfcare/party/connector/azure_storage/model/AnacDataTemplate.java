package it.pagopa.selfcare.party.connector.azure_storage.model;

import com.opencsv.bean.CsvBindByName;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collections;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "taxCode")
public class AnacDataTemplate implements Station {
    @CsvBindByName(column = "codiceIPA")
    private String originId;
    @CsvBindByName(column = "codiceFiscaleGestore")
    private String taxCode;
    @CsvBindByName(column = "denominazioneGestore")
    private String description;
    @CsvBindByName(column = "PEC")
    private String digitalAddress;
    @CsvBindByName(column = "ANAC_incaricato")
    private boolean anacEngaged;
    @CsvBindByName(column = "ANAC_abilitato")
    private boolean anacEnabled;

    public String getTaxCode() {
        if(this.taxCode.length() < 11) {
            return String.join("", Collections.nCopies(11 - taxCode.length(), "0")) + this.taxCode;
        }
        return this.taxCode;
    }
}