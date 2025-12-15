package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import lombok.Data;

@Data
public class SearchField {

    private String name;
    private String type;
    private boolean key;
    private boolean searchable;
    private boolean filterable;
    private boolean sortable;
    private boolean facetable;
    private boolean retrievable;
}