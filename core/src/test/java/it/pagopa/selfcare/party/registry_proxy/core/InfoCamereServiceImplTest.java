package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereBatchRequestConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GetBusinessesByLegal;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchRequest;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {InfoCamereServiceImpl.class})
@ExtendWith(SpringExtension.class)
class InfoCamereServiceImplTest {
    @MockBean
    private InfoCamereConnector infoCamereConnector;

    @Autowired
    private InfoCamereServiceImpl infoCamereServiceImpl;

    @Mock
    private InfoCamereBatchRequestConnector infoCamereBatchRequestConnector;

    @InjectMocks
    private InfoCamereServiceImpl iniPecServiceImpl;

    /**
     * Method under test: {@link InfoCamereServiceImpl#businessesByLegal(GetBusinessesByLegal)}
     */
    @Test
    void testBusinessesByLegal() {
        Businesses businesses = new Businesses();
        businesses.setBusinesses(new ArrayList<>());
        businesses.setLegalTaxId("42");
        businesses.setRequestDateTime("2020-03-01");
        when(infoCamereConnector.businessesByLegal((GetBusinessesByLegal) any())).thenReturn(businesses);

        GetBusinessesByLegal getBusinessesByLegal = new GetBusinessesByLegal();
        getBusinessesByLegal.setLegalTaxId("42");
        assertSame(businesses, infoCamereServiceImpl.businessesByLegal(getBusinessesByLegal));
        verify(infoCamereConnector).businessesByLegal((GetBusinessesByLegal) any());
    }

    @Test
    void testCreateBatchRequestByCfAndCorrelationId() {
        InfoCamereBatchRequest infoCamereBatchRequest = new InfoCamereBatchRequest();
        infoCamereBatchRequest.setId("id");
        infoCamereBatchRequest.setBatchId("batchId");
        infoCamereBatchRequest.setCf("cf");
        infoCamereBatchRequest.setRetry(0);
        infoCamereBatchRequest.setStatus("status");
        infoCamereBatchRequest.setCorrelationId("correlationId");
        infoCamereBatchRequest.setLastReserved(LocalDateTime.now());
        infoCamereBatchRequest.setTimeStamp(LocalDateTime.now());
        infoCamereBatchRequest.setTtl(LocalDateTime.now());

        when(infoCamereBatchRequestConnector.save(any())).thenReturn(infoCamereBatchRequest);

        assertNotNull(iniPecServiceImpl.createBatchRequestByCf("cf"));
    }
}

