package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Business;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.web.model.BusinessesResource;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class BusinessesMapperTest {
    /**
     * Method under test: {@link BusinessesMapper#toResource(Businesses)}
     */
    @Test
    void testToResourceNull() { assertNull(BusinessesMapper.toResource(null)); }

    /**
     * Method under test: {@link BusinessesMapper#toResource(Businesses)}
     */
    @Test
    void testToResource2() {
        Businesses businesses = new Businesses();
        ArrayList<Business> businessList = new ArrayList<>();
        businesses.setBusinesses(businessList);
        businesses.setLegalTaxId("42");
        businesses.setRequestDateTime("2020-03-01");
        BusinessesResource actualToResourceResult = BusinessesMapper.toResource(businesses);
        assertEquals(businessList, actualToResourceResult.getBusinesses());
        assertEquals("2020-03-01", actualToResourceResult.getRequestDateTime());
        assertEquals("42", actualToResourceResult.getLegalTaxId());
    }
}

