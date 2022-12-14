package it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BatchStatusTest {

    @Test
    void testFromValue() {
        assertThrows(IllegalArgumentException.class, () -> BatchStatus.fromValue("42"));
        assertEquals(BatchStatus.NO_BATCH_ID, BatchStatus.fromValue("NO_BATCH_ID"));
        assertEquals(BatchStatus.NOT_WORKED, BatchStatus.fromValue("NOT_WORKED"));
        assertEquals(BatchStatus.WORKING, BatchStatus.fromValue("WORKING"));
        assertEquals("WORKED", BatchStatus.WORKED.toString());
        assertEquals("ERROR", BatchStatus.ERROR.getValue());

    }
}

