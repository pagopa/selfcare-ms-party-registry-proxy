package it.pagopa.selfcare.party.registry_proxy.connector.lucene.model;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;
import lombok.Data;

@Data
public class UOEntity implements UO {

    private Origin origin;
    private String codiceIpa;
    private String denominazioneEnte;
    private String codiceFiscaleEnte;
    private String codiceUniUo;
    private String codiceUniUoPadre;
    private String codiceUniAoo;
    private String descrizioneUo;
    private String mail1;

}
