package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import lombok.Data;

import java.util.List;

@Data
public class SearchIndexDefinition {

    private String name;
    private List<SearchField> fields;
}