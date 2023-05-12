package it.pagopa.selfcare.party.registry_proxy.connector.api;


import it.pagopa.selfcare.party.registry_proxy.connector.model.GeographicTaxonomies;

import java.util.List;

public interface GeoTaxonomiesConnector {

    List<GeographicTaxonomies> getExtByDescription(String description, Integer offset, Integer limit);
}
