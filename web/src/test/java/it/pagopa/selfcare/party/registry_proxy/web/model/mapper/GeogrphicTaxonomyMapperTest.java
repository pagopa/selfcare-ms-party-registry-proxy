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
    void toResourceList_null() {
        //given
        final List<GeographicTaxonomy> geographicTaxonomyList = null;
        //when
        final List<GeographicTaxonomyResource> geographicTaxonomyResourceList = GeographicTaxonomyMapper.toResourceList(geographicTaxonomyList);
        //then
        for(GeographicTaxonomyResource geographicTaxonomyResource : geographicTaxonomyResourceList) {
            assertNull(geographicTaxonomyResource);
        }
    }

    @Test
    void toResourceList_null2() {
        //given
        final List<GeographicTaxonomy> geographicTaxonomyList = mockInstance(new ArrayList<>());
        GeographicTaxonomy geographicTaxonomy = null;
        geographicTaxonomyList.add(geographicTaxonomy);
        //when
        final List<GeographicTaxonomyResource> geographicTaxonomyResourceList = GeographicTaxonomyMapper.toResourceList(geographicTaxonomyList);
        //then
        for(GeographicTaxonomyResource geographicTaxonomyResource : geographicTaxonomyResourceList) {
            assertNull(geographicTaxonomyResource);
        }
    }


    @Test
    void toResourceList() {
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
        final List<GeographicTaxonomyResource> geographicTaxonomyResourceList = GeographicTaxonomyMapper.toResourceList(geographicTaxonomyList);
        //then
        assertNotNull(geographicTaxonomyResourceList);
        reflectionEqualsByName(geographicTaxonomyList, geographicTaxonomyResourceList);
    }

    @Test
    void toResource() {
        //given
        String code = "058";
        GeographicTaxonomy geographicTaxonomy = new GeographicTaxonomy();
        geographicTaxonomy.setDescription("Roma");
        geographicTaxonomy.setGeotaxId(code);
        geographicTaxonomy.setEnabled(true);
        geographicTaxonomy.setRegionId("12");
        geographicTaxonomy.setProvinceId("058");
        geographicTaxonomy.setProvinceAbbreviation("RM");
        geographicTaxonomy.setCountry("100");
        geographicTaxonomy.setCountryAbbreviation("IT");
        geographicTaxonomy.setIstatCode("null");

        //when
        final GeographicTaxonomyResource geographicTaxonomyResource = GeographicTaxonomyMapper.toResource(geographicTaxonomy);
        //then
        assertNotNull(geographicTaxonomyResource);
        reflectionEqualsByName(geographicTaxonomy, geographicTaxonomyResource);
    }

    @Test
    void toResource_null() {
        //given
        GeographicTaxonomy geographicTaxonomy = null;


        //when
        final GeographicTaxonomyResource geographicTaxonomyResource = GeographicTaxonomyMapper.toResource(geographicTaxonomy);
        //then
        assertNull(geographicTaxonomyResource);
    }
}
