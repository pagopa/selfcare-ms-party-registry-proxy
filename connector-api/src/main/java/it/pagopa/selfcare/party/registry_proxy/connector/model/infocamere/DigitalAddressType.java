package it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DigitalAddressType {

    PEC("PEC"),
    REM("REM"),
    SERCQ("SERCQ");

    private String value;

    DigitalAddressType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static DigitalAddressType fromValue(String value) {
        for (DigitalAddressType b : DigitalAddressType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
