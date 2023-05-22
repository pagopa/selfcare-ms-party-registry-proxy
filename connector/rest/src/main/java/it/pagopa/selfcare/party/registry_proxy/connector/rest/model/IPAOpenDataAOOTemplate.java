package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import com.opencsv.bean.CsvBindByName;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "codiceUniAoo")
public abstract class IPAOpenDataAOOTemplate implements AOO {

    @CsvBindByName(column = "Codice_IPA")
    private String codiceIpa;
    @CsvBindByName(column = "Denominazione_ente")
    private String denominazioneEnte;
    @CsvBindByName(column = "Codice_fiscale_ente")
    private String codiceFiscaleEnte;
    @CsvBindByName(column = "Codice_uni_aoo")
    private String codiceUniAoo;
    @CsvBindByName(column = "Denominazione_aoo")
    private String denominazioneAoo;
    @CsvBindByName(column = "Mail1")
    private String mail1;
    @CsvBindByName(column = "cod_aoo")
    private String codAoo;

}
