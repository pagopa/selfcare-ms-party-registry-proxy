package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.GeographicTaxonomies;

import java.util.List;

public interface GeographicTaxonomiesService {

    List<GeographicTaxonomies> retrieveGeoTaxonomiesByDescription(String description, Integer offset, Integer limit);

}
