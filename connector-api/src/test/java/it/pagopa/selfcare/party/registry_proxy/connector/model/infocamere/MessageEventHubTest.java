package it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class MessageEventHubTest {

    @Test
    void testConstructor() {
        MessageEventHub actualMessageEventHub = new MessageEventHub();
        actualMessageEventHub.setCorrelationId("42");
        DigitalAddress digitalAddress = new DigitalAddress("Type", "42 Main St");

        actualMessageEventHub.setPrimaryDigitalAddress(digitalAddress);
        PhysicalAddress physicalAddress = new PhysicalAddress();
        actualMessageEventHub.setPrimaryPhysicalAddress(physicalAddress);
        ArrayList<DigitalAddress> digitalAddressList = new ArrayList<>();
        actualMessageEventHub.setSecondaryDigitalAddresses(digitalAddressList);
        ArrayList<PhysicalAddress> physicalAddressList = new ArrayList<>();
        actualMessageEventHub.setSecondaryPhysicalAddresses(physicalAddressList);
        actualMessageEventHub.setTaxId("42");
        actualMessageEventHub.toString();
        assertEquals("42", actualMessageEventHub.getCorrelationId());
        assertSame(digitalAddress, actualMessageEventHub.getPrimaryDigitalAddress());
        assertSame(physicalAddress, actualMessageEventHub.getPrimaryPhysicalAddress());
        ArrayList<DigitalAddress> secondaryDigitalAddresses = actualMessageEventHub.getSecondaryDigitalAddresses();
        assertSame(digitalAddressList, secondaryDigitalAddresses);
        ArrayList<PhysicalAddress> secondaryPhysicalAddresses = actualMessageEventHub.getSecondaryPhysicalAddresses();
        assertEquals(secondaryPhysicalAddresses, secondaryDigitalAddresses);
        assertSame(physicalAddressList, secondaryPhysicalAddresses);
        assertEquals(digitalAddressList, secondaryPhysicalAddresses);
        assertEquals("42", actualMessageEventHub.getTaxId());
    }

}

