package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.commons.base.logging.LogUtils;
import it.pagopa.selfcare.party.registry_proxy.connector.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GeographicTaxonomies;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.GeoTaxonomiesRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.geotaxonomy.GeographicTaxonomiesResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.geotaxonomy.GeographicTaxonomyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GeoTaxonomiesConnectorImpl implements GeoTaxonomiesConnector {

    private final GeoTaxonomiesRestClient restClient;
    @Autowired
    public GeoTaxonomiesConnectorImpl(GeoTaxonomiesRestClient restClient) {
        this.restClient = restClient;
    }


    @Override
    public List<GeographicTaxonomies> getExtByDescription(String description, String offset, String limit) {
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "getExtByDescription description = {}", description);
        Assert.hasText(description, "Description is required");
        GeographicTaxonomiesResponse result = restClient.getExtByDescription(description, offset, limit);
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "getExtByDescription result = {}", result);
        return toGeoTaxonomiesList(result.getGeographicTaxonomiesResponse());
    }


    private List<GeographicTaxonomies> toGeoTaxonomiesList(List<GeographicTaxonomyResponse> result) {
        List<GeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        for(GeographicTaxonomyResponse geoTaxonomiesResult : result) {
            GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
            geographicTaxonomies.setDescription(geoTaxonomiesResult.getDescription());
            geographicTaxonomies.setGeotaxId(geoTaxonomiesResult.getGeotaxId());
            geographicTaxonomies.setEnabled(geoTaxonomiesResult.isEnabled());
            geographicTaxonomies.setRegionId(geoTaxonomiesResult.getRegionId());
            geographicTaxonomies.setProvinceId(geoTaxonomiesResult.getProvinceId());
            geographicTaxonomies.setProvinceAbbreviation(geoTaxonomiesResult.getProvinceAbbreviation());
            geographicTaxonomies.setCountry(geoTaxonomiesResult.getCountry());
            geographicTaxonomies.setCountryAbbreviation(geoTaxonomiesResult.getCountryAbbreviation());
            geographicTaxonomies.setIstatCode(geoTaxonomiesResult.getIstatCode());
            geographicTaxonomiesList.add(geographicTaxonomies);
        }
        return geographicTaxonomiesList;
    }
}
