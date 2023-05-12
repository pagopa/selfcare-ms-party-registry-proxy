package it.pagopa.selfcare.party.registry_proxy.core;


import it.pagopa.selfcare.party.registry_proxy.connector.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GeographicTaxonomies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
@Service
@Slf4j
public class GeographicTaxonomiesServiceImpl implements GeographicTaxonomiesService{
    private final GeoTaxonomiesConnector geoTaxonomiesConnector;

    public GeographicTaxonomiesServiceImpl(GeoTaxonomiesConnector geoTaxonomiesConnector) {
        this.geoTaxonomiesConnector = geoTaxonomiesConnector;
    }


    @Override
    public List<GeographicTaxonomies> retrieveGeoTaxonomiesByDescription(String description, String offset, String limit) {
        Assert.hasText(description, "Description is required");
        return geoTaxonomiesConnector.getExtByDescription(description, offset, limit);
    }



}
