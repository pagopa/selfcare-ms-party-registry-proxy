package it.pagopa.selfcare.party.registry_proxy.connector.model;

public interface UO {

    default String getId() {
        return createId(getOrigin(), getCodiceUniUo());
    }

    String getCodiceIpa();

    String getDenominazioneEnte();

    String getCodiceFiscaleEnte();

    String getCodiceUniUo();

    String getCodiceUniUoPadre();

    String getCodiceUniAoo();

    String getDescrizioneUo();

    String getMail1();
    Origin getOrigin();


    enum Field implements SearchField {
        ID("id"),
        CODICE_IPA("codiceIpa"),
        DENOMINAZIONE_ENTE("denominazioneEnte"),
        CODICE_FISCALE_ENTE("codiceFiscaleEnte"),
        CODICE_UNI_UO("codiceUniUo"),
        CODICE_UNI_UO_PADRE("codiceUniUoPadre"),
        CODICE_UNI_AOO("codiceUniAoo"),
        DESCRIZIONE_UO("descrizioneUo"),
        MAIL1("mail1"),
        ORIGIN("origin");

        private final String name;

        Field(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    static String createId(Origin origin, String codUo) {
        return origin + "_UO_" + codUo;
    }

}
