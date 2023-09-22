package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import com.opencsv.bean.CsvBindByName;
import it.pagopa.selfcare.party.registry_proxy.connector.model.PDND;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public abstract class OpenDataPDNDTemplate implements PDND {

    private String id;
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