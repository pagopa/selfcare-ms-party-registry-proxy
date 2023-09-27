package it.pagopa.selfcare.party.connector.azure_storage.model;

import com.opencsv.bean.CsvBindByName;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "taxCode")
public class AnacDataTemplate implements Station {
    @CsvBindByName(column = "codice_IPA")
    private String originId;
    @CsvBindByName(column = "cf_gestore")
    private String taxCode;
    @CsvBindByName(column = "denominazione")
    private String description;
    @CsvBindByName(column = "domicilio_digitale")
    private String digitalAddress;
    @CsvBindByName(column = "anac_incaricato")
    private boolean anacEngaged;
    @CsvBindByName(column = "anac_abilitato")
    private boolean anacEnabled;
}