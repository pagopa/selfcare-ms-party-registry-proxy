package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;


import it.pagopa.selfcare.party.registry_proxy.connector.model.GeographicTaxonomy;
import it.pagopa.selfcare.party.registry_proxy.web.model.GeographicTaxonomyResource;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static it.pagopa.selfcare.commons.utils.TestUtils.reflectionEqualsByName;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GeogrphicTaxonomyMapperTest {
    @Test
    void toResource_null() {
        //given
        final List<GeographicTaxonomy> geographicTaxonomyList = null;
        //when
        final List<GeographicTaxonomyResource> geographicTaxonomyResourceList = GeographicTaxonomyMapper.toResource(geographicTaxonomyList);
        //then
        for(GeographicTaxonomyResource geographicTaxonomyResource : geographicTaxonomyResourceList) {
            assertNull(geographicTaxonomyResource);
        }
    }

    @Test
    void toResource_null2() {
        //given
        final List<GeographicTaxonomy> geographicTaxonomyList = mockInstance(new ArrayList<>());
        GeographicTaxonomy geographicTaxonomy = null;
        geographicTaxonomyList.add(geographicTaxonomy);
        //when
        final List<GeographicTaxonomyResource> geographicTaxonomyResourceList = GeographicTaxonomyMapper.toResource(geographicTaxonomyList);
        //then
        for(GeographicTaxonomyResource geographicTaxonomyResource : geographicTaxonomyResourceList) {
            assertNull(geographicTaxonomyResource);
        }
    }


    @Test
    void toResource() {
        //given
        String description = "Roma";
        final List<GeographicTaxonomy> geographicTaxonomyList = mockInstance(new ArrayList<>());
        GeographicTaxonomy geographicTaxonomy = new GeographicTaxonomy();
        geographicTaxonomy.setDescription(description);
        geographicTaxonomy.setGeotaxId("058");
        geographicTaxonomy.setEnabled(true);
        geographicTaxonomy.setRegionId("12");
        geographicTaxonomy.setProvinceId("058");
        geographicTaxonomy.setProvinceAbbreviation("RM");
        geographicTaxonomy.setCountry("100");
        geographicTaxonomy.setCountryAbbreviation("IT");
        geographicTaxonomy.setIstatCode("null");
        geographicTaxonomyList.add(geographicTaxonomy);

        //when
        final List<GeographicTaxonomyResource> geographicTaxonomyResourceList = GeographicTaxonomyMapper.toResource(geographicTaxonomyList);
        //then
        assertNotNull(geographicTaxonomyResourceList);
        reflectionEqualsByName(geographicTaxonomyList, geographicTaxonomyResourceList);
    }
}
