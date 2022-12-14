package it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere;

import lombok.Data;

import java.util.List;

@Data
public class Pec {
    private String cf;
    private String pecImpresa;
    private List<String> pecProfessionistas;
}
