package it.pagopa.selfcare.party.registry_proxy.connector.model;

public interface AOO {

    default String getId() {
        return getCodiceUniAoo();
    }
    String getCodiceIpa();

    String getDenominazioneEnte();

    String getCodiceFiscaleEnte();

    String getCodiceUniAoo();

    String getDenominazioneAoo();

    String getMail1();

    String getCodAoo();

    Origin getOrigin();


    enum Field implements SearchField {
        ID("id"),
        CODICE_IPA("codiceIpa"),
        DENOMINAZIONE_ENTE("denominazioneEnte"),
        CODICE_FISCALE_ENTE("codiceFiscaleEnte"),
        CODICE_UNI_AOO("codiceUniAoo"),
        DENOMINAZIONE_AOO("denominazioneAoo"),
        MAIL1("mail1"),
        COD_AOO("codAoo"),
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

}
