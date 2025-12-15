package it.pagopa.selfcare.party.registry_proxy.connector.model;

import java.util.List;
import lombok.Data;

@Data
public class SearchIndexDefinition {

    private String name;
    private List<AzureSearchField> fields;
}