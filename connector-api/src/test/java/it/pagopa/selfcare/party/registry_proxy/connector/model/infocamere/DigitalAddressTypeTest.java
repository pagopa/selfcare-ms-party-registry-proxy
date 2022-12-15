package it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DigitalAddressTypeTest {

    @Test
    void testFromValue() {
        assertThrows(IllegalArgumentException.class, () -> DigitalAddressType.fromValue("42"));
        assertEquals("PEC", DigitalAddressType.PEC.toString());
        assertEquals(DigitalAddressType.REM, DigitalAddressType.fromValue("REM"));
        assertEquals(DigitalAddressType.SERCQ, DigitalAddressType.fromValue("SERCQ"));
    }
}

