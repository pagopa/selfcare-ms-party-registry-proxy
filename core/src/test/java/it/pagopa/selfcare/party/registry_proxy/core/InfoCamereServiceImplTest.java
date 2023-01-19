package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereBatchRequestConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereLegalAddress;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereLocationAddress;

import java.time.LocalDate;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class InfoCamereServiceImplTest {
    @Mock
    private InfoCamereConnector infoCamereConnector;
    @Mock
    private InfoCamereBatchRequestConnector infoCamereBatchRequestConnector;

    @InjectMocks
    private InfoCamereServiceImpl infoCamereServiceImpl;

    /**
     * Method under test: {@link InfoCamereServiceImpl#institutionsByLegalTaxId(String)}
     */
    @Test
    void testInstitutionsByLegalTaxId() {
        Businesses businesses = new Businesses();
        businesses.setBusinesses(new ArrayList<>());
        businesses.setLegalTaxId("42");
        businesses.setRequestDateTime("2020-03-01");
        when(infoCamereConnector.institutionsByLegalTaxId(any())).thenReturn(businesses);

        assertSame(businesses, infoCamereServiceImpl.institutionsByLegalTaxId("42"));
        verify(infoCamereConnector).institutionsByLegalTaxId(any());
    }

    @Test
    void testCreateBatchRequestByCfAndCorrelationId() {
        InfoCamereBatchRequest infoCamereBatchRequest = new InfoCamereBatchRequest();
        infoCamereBatchRequest.setId("id");
        infoCamereBatchRequest.setBatchId("batchId");
        infoCamereBatchRequest.setCf("cf");
        infoCamereBatchRequest.setRetry(0);
        infoCamereBatchRequest.setStatus("status");
        infoCamereBatchRequest.setLastReserved(LocalDateTime.now());
        infoCamereBatchRequest.setTimeStamp(LocalDateTime.now());
        infoCamereBatchRequest.setTtl(LocalDateTime.now());

        when(infoCamereBatchRequestConnector.save(any())).thenReturn(infoCamereBatchRequest);

        assertNotNull(infoCamereServiceImpl.createBatchRequestByCf("cf"));
    }

    /**
     * Method under test: {@link InfoCamereServiceImpl#legalAddressByTaxId(String)}
     */
    @Test
    void testLegalAddressByTaxId() {
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
        infoCamereLegalAddress.setDateTimeExtraction(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        infoCamereLegalAddress.setLegalAddress(infoCamereLocationAddress);
        infoCamereLegalAddress.setTaxId("42");
        when(infoCamereConnector.legalAddressByTaxId(any())).thenReturn(infoCamereLegalAddress);
        assertSame(infoCamereLegalAddress, infoCamereServiceImpl.legalAddressByTaxId("42"));
        verify(infoCamereConnector).legalAddressByTaxId(any());
    }
}

