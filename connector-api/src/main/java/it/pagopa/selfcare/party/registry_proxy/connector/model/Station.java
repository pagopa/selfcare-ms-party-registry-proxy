package it.pagopa.selfcare.party.registry_proxy.connector.model;


public interface Station {

    default String getId() {
        return createId(getOrigin(), getTaxCode());
    }

    String getOriginId();

    String getTaxCode();

    String getDescription();

    String getDigitalAddress();

    boolean isAnacEngaged();

    boolean isAnacEnabled();

    default Origin getOrigin() {
        return Origin.ANAC;
    }

    enum Field implements SearchField {
        ID("id"),
        TAX_CODE("taxCode"),
        DESCRIPTION("description"),
        ORIGIN("origin"),
        ANAC_ENABLED("anacEnabled"),
        ANAC_ENGAGED("anacEngaged"),
        DIGITAL_ADDRESS("digitalAddress");

        private final String name;

        Field(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    static String createId(Origin origin, String code) {
        return origin + "_" + code;
    }

}
