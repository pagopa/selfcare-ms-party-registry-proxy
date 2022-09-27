package it.pagopa.selfcare.party.registry_proxy.connector.model;

public interface Institution {

    String getId();

    String getOriginId();

    String getO();

    String getOu();

    String getAoo();

    String getTaxCode();

    String getCategory();

    String getDescription();

    String getDigitalAddress();

    String getAddress();

    String getZipCode();

    Origin getOrigin();

}
