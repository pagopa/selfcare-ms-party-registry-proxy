package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import com.opencsv.bean.CsvBindByName;
import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "codiceUniUo")
public abstract class IPAOpenDataUOTemplate implements UO {

    @CsvBindByName(column = "Codice_IPA")
    private String codiceIpa;
    @CsvBindByName(column = "Denominazione_ente")
    private String denominazioneEnte;
    @CsvBindByName(column = "Codice_fiscale_ente")
    private String codiceFiscaleEnte;
    @CsvBindByName(column = "Codice_uni_uo")
    private String codiceUniUo;
    @CsvBindByName(column = "Codice_uni_uo_padre")
    private String codiceUniUoPadre;
    @CsvBindByName(column = "Codice_uni_aoo")
    private String codiceUniAoo;
    @CsvBindByName(column = "Descrizione_uo")
    private String descrizioneUo;
    @CsvBindByName(column = "Mail1")
    private String mail1;


}
