package it.pagopa.selfcare.party.registry_proxy.connector.model;

import lombok.Data;

import java.util.List;

@Data
public class Businesses {
    private String requestDateTime;
    private String legalTaxId;
    private List<Business> businesses;
}
