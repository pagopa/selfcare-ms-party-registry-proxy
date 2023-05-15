package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;


import it.pagopa.selfcare.party.registry_proxy.connector.model.GeographicTaxonomy;
import it.pagopa.selfcare.party.registry_proxy.web.model.GeographicTaxonomyResource;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GeographicTaxonomyMapper {
    public static List<GeographicTaxonomyResource> toResource(List<GeographicTaxonomy> geographicTaxonomyList) {
        log.trace("toResource start");
        log.debug("toResource entity = {}", geographicTaxonomyList);
        List<GeographicTaxonomyResource> geographicTaxonomyResourceList = new ArrayList<>();
        GeographicTaxonomyResource geographicTaxonomyResource = null;
        if (geographicTaxonomyList != null) {
            for (GeographicTaxonomy geographicTaxonomy : geographicTaxonomyList) {
                if(geographicTaxonomy != null) {
                    geographicTaxonomyResource = new GeographicTaxonomyResource();
                    geographicTaxonomyResource.setDescription(geographicTaxonomy.getDescription());
                    geographicTaxonomyResource.setGeotaxId(geographicTaxonomy.getGeotaxId());
                    geographicTaxonomyResource.setEnabled(geographicTaxonomy.isEnabled());
                    geographicTaxonomyResource.setRegionId(geographicTaxonomy.getRegionId());
                    geographicTaxonomyResource.setProvinceId(geographicTaxonomy.getProvinceId());
                    geographicTaxonomyResource.setProvinceAbbreviation(geographicTaxonomy.getProvinceAbbreviation());
                    geographicTaxonomyResource.setCountry(geographicTaxonomy.getCountry());
                    geographicTaxonomyResource.setCountryAbbreviation(geographicTaxonomy.getCountryAbbreviation());
                    geographicTaxonomyResource.setIstatCode(geographicTaxonomy.getIstatCode());
                    geographicTaxonomyResourceList.add(geographicTaxonomyResource);
                } else {
                    geographicTaxonomyResourceList.add(geographicTaxonomyResource);
                }
            }
            log.debug("toResource result = {}", geographicTaxonomyResourceList);
            log.trace("toResource end");
        }
        return geographicTaxonomyResourceList;
    }
}
