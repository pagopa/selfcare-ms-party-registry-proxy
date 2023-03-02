package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AdEResultCodeEnum {

        _00("00"),

        _01("01"),

        _02("02");

        private String value;

        AdEResultCodeEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
}
