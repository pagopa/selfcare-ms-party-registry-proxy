package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereLegalAddress;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereLocationAddress;
import it.pagopa.selfcare.party.registry_proxy.web.model.LegalAddressResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.LocationAddress;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;

class LegalAddressMapperTest {
    /**
     * Method under test: {@link LegalAddressMapper#toResource(InfoCamereLegalAddress)}
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

        InfoCamereLegalAddress infoCamereLegalAddress = new InfoCamereLegalAddress();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date fromResult = Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant());
        infoCamereLegalAddress.setDateTimeExtraction(fromResult);
        infoCamereLegalAddress.setLegalAddress(infoCamereLocationAddress);
        infoCamereLegalAddress.setTaxId("42");
        LegalAddressResource actualToResourceResult = LegalAddressMapper.toResource(infoCamereLegalAddress);
        assertSame(fromResult, actualToResourceResult.getDateTimeExtraction());
        assertEquals("42", actualToResourceResult.getTaxId());
        LocationAddress legalAddress = actualToResourceResult.getLegalAddress();
        assertEquals("Toponym Street 42", legalAddress.getAddress());
        assertEquals("Postal Code", legalAddress.getZip());
        assertEquals("Province", legalAddress.getProvince());
        assertEquals("Municipality", legalAddress.getMunicipality());
        assertEquals("42 Main St", legalAddress.getDescription());
    }

    /**
     * Method under test: {@link LegalAddressMapper#toResource(InfoCamereLegalAddress)}
     */
    @Test
    void testToResource2() {
        assertNull(LegalAddressMapper.toResource(null));
    }
}

