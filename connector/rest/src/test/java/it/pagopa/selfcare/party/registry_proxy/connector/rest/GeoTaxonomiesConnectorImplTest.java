package it.pagopa.selfcare.party.registry_proxy.connector.rest;



import it.pagopa.selfcare.party.registry_proxy.connector.model.GeographicTaxonomies;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.GeoTaxonomiesRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.geotaxonomy.GeographicTaxonomiesResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.geotaxonomy.GeographicTaxonomyResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class GeoTaxonomiesConnectorImplTest {
    @InjectMocks
    private GeoTaxonomiesConnectorImpl geoTaxonomiesConnectorImpl;

    @Mock
    private GeoTaxonomiesRestClient geoTaxonomiesRestClient;


    @Test
    void testGetExtByDescription() {

        //given
        String description = "Roma";
        GeographicTaxonomiesResponse geographicTaxonomiesResponse = new GeographicTaxonomiesResponse();
        GeographicTaxonomyResponse geographicTaxonomyResponse = new GeographicTaxonomyResponse();
        geographicTaxonomyResponse.setDescription(description);
        geographicTaxonomyResponse.setGeotaxId("058");
        geographicTaxonomyResponse.setEnabled(true);
        geographicTaxonomyResponse.setRegionId("12");
        geographicTaxonomyResponse.setProvinceId("058");
        geographicTaxonomyResponse.setProvinceAbbreviation("RM");
        geographicTaxonomyResponse.setCountry("100");
        geographicTaxonomyResponse.setCountryAbbreviation("IT");
        geographicTaxonomyResponse.setIstatCode("null");
        List<GeographicTaxonomyResponse> geographicTaxonomyResponseList = new ArrayList<>();
        geographicTaxonomyResponseList.add(geographicTaxonomyResponse);
        geographicTaxonomiesResponse.setGeographicTaxonomiesResponse(geographicTaxonomyResponseList);

        List<GeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
        geographicTaxonomies.setDescription(description);
        geographicTaxonomies.setGeotaxId("058");
        geographicTaxonomies.setEnabled(true);
        geographicTaxonomies.setRegionId("12");
        geographicTaxonomies.setProvinceId("058");
        geographicTaxonomies.setProvinceAbbreviation("RM");
        geographicTaxonomies.setCountry("100");
        geographicTaxonomies.setCountryAbbreviation("IT");
        geographicTaxonomies.setIstatCode("null");
        geographicTaxonomiesList.add(geographicTaxonomies);

        when(geoTaxonomiesRestClient.getExtByDescription(anyString(),anyString(),anyString())).thenReturn(geographicTaxonomiesResponse);

        //when
        geographicTaxonomiesList = geoTaxonomiesConnectorImpl.getExtByDescription(description, "0", "10");

        //then
        assertNotNull(geographicTaxonomiesList);
        assertNotNull(geographicTaxonomiesList.getClass());
        assertEquals(1, geographicTaxonomiesList.size());
        GeographicTaxonomies geographicTaxonomy = geographicTaxonomiesList.iterator().next();
        assertEquals(geographicTaxonomyResponse.getDescription(), geographicTaxonomy.getDescription());
        assertEquals(geographicTaxonomyResponse.getIstatCode(), geographicTaxonomy.getIstatCode());
        assertEquals(geographicTaxonomyResponse.getProvinceId(), geographicTaxonomy.getProvinceId());
        assertEquals(geographicTaxonomyResponse.getProvinceAbbreviation(), geographicTaxonomy.getProvinceAbbreviation());
        assertEquals(geographicTaxonomyResponse.getRegionId(), geographicTaxonomy.getRegionId());
        assertEquals(geographicTaxonomyResponse.getGeotaxId(), geographicTaxonomy.getGeotaxId());
        assertEquals(geographicTaxonomyResponse.getCountry(), geographicTaxonomy.getCountry());
        assertEquals(geographicTaxonomyResponse.getCountryAbbreviation(), geographicTaxonomy.getCountryAbbreviation());

        verify(geoTaxonomiesRestClient, times(1))
                .getExtByDescription(anyString(),anyString(),anyString());
        verifyNoMoreInteractions(geoTaxonomiesRestClient);

    }

    @Test
    void testGetExtByDescription_nullDescription() {

        //given
        String description = null;
        GeographicTaxonomiesResponse geographicTaxonomiesResponse = new GeographicTaxonomiesResponse();
        GeographicTaxonomyResponse geographicTaxonomyResponse = new GeographicTaxonomyResponse();
        geographicTaxonomyResponse.setDescription(description);
        geographicTaxonomyResponse.setGeotaxId("058");
        geographicTaxonomyResponse.setEnabled(true);
        geographicTaxonomyResponse.setRegionId("12");
        geographicTaxonomyResponse.setProvinceId("058");
        geographicTaxonomyResponse.setProvinceAbbreviation("RM");
        geographicTaxonomyResponse.setCountry("100");
        geographicTaxonomyResponse.setCountryAbbreviation("IT");
        geographicTaxonomyResponse.setIstatCode("null");
        List<GeographicTaxonomyResponse> geographicTaxonomyResponseList = new ArrayList<>();
        geographicTaxonomyResponseList.add(geographicTaxonomyResponse);
        geographicTaxonomiesResponse.setGeographicTaxonomiesResponse(geographicTaxonomyResponseList);

        when(geoTaxonomiesRestClient.getExtByDescription(anyString(),anyString(),anyString())).thenReturn(geographicTaxonomiesResponse);

        //when
        Executable executable = () -> geoTaxonomiesConnectorImpl.getExtByDescription(description, "0", "10");

        //then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Description is required", e.getMessage());
        Mockito.verifyNoInteractions(geoTaxonomiesRestClient);

    }


}

