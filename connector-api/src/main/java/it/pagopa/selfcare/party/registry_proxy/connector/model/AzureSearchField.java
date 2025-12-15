package it.pagopa.selfcare.party.registry_proxy.connector.model;

import lombok.Data;

@Data
public class AzureSearchField {

    private String name;
    private String type;
    private boolean key;
    private boolean searchable;
    private boolean filterable;
    private boolean sortable;
    private boolean facetable;
    private boolean retrievable;

    public static AzureSearchField field(
            String name,
            String type,
            boolean key,
            boolean searchable,
            boolean filterable,
            boolean sortable) {

        AzureSearchField f = new AzureSearchField();
        f.setName(name);
        f.setType(type);
        f.setKey(key);
        f.setSearchable(searchable);
        f.setFilterable(filterable);
        f.setSortable(sortable);
        f.setFacetable(false);
        f.setRetrievable(true);
        return f;
    }
}