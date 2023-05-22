package it.pagopa.selfcare.party.registry_proxy.web.model;

import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import lombok.Data;

@Data
public class DummyAOO implements AOO {

    private String id;
    private String codiceIpa;
    private String denominazioneEnte;
    private String codiceFiscaleEnte;
    private String codiceUniAoo;
    private String denominazioneAoo;
    private String mail1;
    private String codAoo;
    private Origin origin;

}