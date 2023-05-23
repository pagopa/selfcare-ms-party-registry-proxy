package it.pagopa.selfcare.party.registry_proxy.connector.model;

import lombok.Data;

@Data
public class DummyUO implements UO {

    private String id;
    private String codiceIpa;
    private String denominazioneEnte;
    private String codiceFiscaleEnte;
    private String codiceUniUo;
    private String codiceUniUoPadre;
    private String codiceUniAoo;
    private String descrizioneUo;
    private String mail1;
    private Origin origin;

}