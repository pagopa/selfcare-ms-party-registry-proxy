package it.pagopa.selfcare.party.registry_proxy.connector.model;

import java.util.Arrays;

public enum Origin {
    MOCK("static"),
    IPA("IPA"),
    INFOCAMERE("INFOCAMERE");

    private final String value;

    Origin(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }


    public static Origin from(String value) {
        return Arrays.stream(values())
                .filter(origin -> origin.toString().equals(value))
                .findAny()
                .orElseThrow();
    }

}
