package it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere;

import lombok.Data;

import java.util.ArrayList;

@Data
public class MessageEventHub {
    private String correlationId;
    private String taxId;
    private DigitalAddress primaryDigitalAddress;
    private PhysicalAddress primaryPhysicalAddress;
    private ArrayList<DigitalAddress> secondaryDigitalAddresses;
    private ArrayList<PhysicalAddress> secondaryPhysicalAddresses;
}
