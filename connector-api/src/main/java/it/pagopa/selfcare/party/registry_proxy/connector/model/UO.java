package it.pagopa.selfcare.party.registry_proxy.connector.model;

public interface UO {

    default String getId() {
        return getCodiceUniUo();
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


    String getDataIstituzione();
    String getNomeResponsabile();
    String getCognomeResponsabile();
    String getMailResponsabile();
    String getTelefonoResponsabile();
    String getCodiceComuneISTAT();
    String getCodiceCatastaleComune();
    String getCAP();
    String getIndirizzo();
    String getTelefono();
    String getFax();
    String getTipoMail1();
    String getUrl();
    String getDataAggiornamento();


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
        ORIGIN("origin"),

        DATA_ISTITUTIONE("dataIstituzione"),
        NOME_RESPONSABILE("nomeResponsabile"),
        COGNOME_RESPONSABILE("cognomeResponsabile"),
        MAIL_RESPONSABILE("mailResponsabile"),
        TELEFONO_RESPONSABILE("telefonoResponsabile"),
        CODICE_COMUNE_ISTAT("codiceComuneISTAT"),
        CODICE_CATASTALE_COMUNE("codiceCatastaleComune"),
        CAP("CAP"),
        INDIRIZZO("indirizzo"),
        TELEFONO("telefono"),
        FAX("fax"),
        TIPO_MAIL1("tipoMail1"),
        URL("url"),
        DATA_AGGIORNAMENTO("dataAggiornamento");

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
