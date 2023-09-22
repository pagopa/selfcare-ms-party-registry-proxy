package it.pagopa.selfcare.party.registry_proxy.connector.model;


public interface PDND {

    String getId();
    String getTaxCode();
    boolean isAnacEngaged();
    String getDigitalAddress();
    String getDescription();
    boolean isAnacEnabled();
    String getOriginId();
    Origin getOrigin();

    enum Field implements SearchField {
        ID("id"),
        TAX_CODE("taxCode"),
        DESCRIPTION("description"),
        ORIGIN_ID("originId"),
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

}
