package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereLocationAddress;
import it.pagopa.selfcare.party.registry_proxy.web.model.LocationAddress;
import org.junit.jupiter.api.Test;

class LocationAddressMapperTest {
    /**
     * Method under test: {@link LocationAddressMapper#toResource(InfoCamereLocationAddress)}
     */
    @Test
    void testToResource() {
        InfoCamereLocationAddress infoCamereLocationAddress = new InfoCamereLocationAddress();
        infoCamereLocationAddress.setAddress("42 Main St");
        infoCamereLocationAddress.setMunicipality("Municipality");
        infoCamereLocationAddress.setPostalCode("Postal Code");
        infoCamereLocationAddress.setProvince("Province");
        infoCamereLocationAddress.setStreet("Street");
        infoCamereLocationAddress.setStreetNumber("42");
        infoCamereLocationAddress.setToponym("Toponym");
        LocationAddress actualToResourceResult = LocationAddressMapper.toResource(infoCamereLocationAddress);
        assertEquals("Toponym Street 42", actualToResourceResult.getAddress());
        assertEquals("Postal Code", actualToResourceResult.getZip());
        assertEquals("Province", actualToResourceResult.getProvince());
        assertEquals("Municipality", actualToResourceResult.getMunicipality());
        assertEquals("42 Main St", actualToResourceResult.getDescription());
    }

    /**
     * Method under test: {@link LocationAddressMapper#toResource(InfoCamereLocationAddress)}
     */
    @Test
    void testToResource2() {
        assertNull(LocationAddressMapper.toResource(null));
    }
}

