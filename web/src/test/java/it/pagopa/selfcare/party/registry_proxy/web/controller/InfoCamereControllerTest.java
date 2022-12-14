package it.pagopa.selfcare.party.registry_proxy.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GetBusinessesByLegal;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.party.registry_proxy.core.InfoCamereService;
import it.pagopa.selfcare.party.registry_proxy.web.model.GetBusinessesByLegalDto;
import it.pagopa.selfcare.party.registry_proxy.web.model.GetDigitalAddressInfoCamereOKDto;
import it.pagopa.selfcare.party.registry_proxy.web.model.GetDigitalAddressInfoCamereRequestBodyDto;
import it.pagopa.selfcare.party.registry_proxy.web.model.GetDigitalAddressInfoCamereRequestBodyFilterDto;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {InfoCamereController.class})
@ExtendWith(SpringExtension.class)
class InfoCamereControllerTest {
    @InjectMocks
    private InfoCamereController infoCamereController;
    @Mock
    private InfoCamereService infoCamereService;

    /**
     * Method under test: {@link InfoCamereController#businessesByLegal(GetBusinessesByLegalDto)}
     */
    @Test
    void testBusinessesByLegal() throws Exception {
        Businesses businesses = new Businesses();
        businesses.setBusinesses(new ArrayList<>());
        businesses.setLegalTaxId("42");
        businesses.setRequestDateTime("2020-03-01");
        when(infoCamereService.businessesByLegal((GetBusinessesByLegal) any())).thenReturn(businesses);

        GetBusinessesByLegalDto getBusinessesByLegalDto = new GetBusinessesByLegalDto();
        getBusinessesByLegalDto.setLegalTaxId("42");
        String content = (new ObjectMapper()).writeValueAsString(getBusinessesByLegalDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/businesses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(infoCamereController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"requestDateTime\":\"2020-03-01\",\"legalTaxId\":\"42\",\"businesses\":[]}"));
    }

    @Test
    void testCreateBatchRequest() {
        InfoCamereBatchRequest infoCamereBatchRequest = new InfoCamereBatchRequest();
        infoCamereBatchRequest.setId("id");
        infoCamereBatchRequest.setBatchId("batchId");
        infoCamereBatchRequest.setCf("taxId");
        infoCamereBatchRequest.setRetry(0);
        infoCamereBatchRequest.setStatus("status");
        infoCamereBatchRequest.setCorrelationId("correlationId");
        infoCamereBatchRequest.setLastReserved(LocalDateTime.now());
        infoCamereBatchRequest.setTimeStamp(LocalDateTime.now());
        infoCamereBatchRequest.setTtl(LocalDateTime.now());

        GetDigitalAddressInfoCamereRequestBodyDto dto = new GetDigitalAddressInfoCamereRequestBodyDto();
        GetDigitalAddressInfoCamereRequestBodyFilterDto filter = new GetDigitalAddressInfoCamereRequestBodyFilterDto();
        filter.setTaxId("taxId");
        dto.setFilter(filter);

        GetDigitalAddressInfoCamereOKDto result = new GetDigitalAddressInfoCamereOKDto();
        result.setCorrelationId("correlationId");

        when(infoCamereService.createBatchRequestByCf(any())).thenReturn(infoCamereBatchRequest);

        GetDigitalAddressInfoCamereOKDto getDigitalAddressInfoCamereOKDto = infoCamereController.createBatchRequest(dto).getBody();
        assert getDigitalAddressInfoCamereOKDto != null;
        assertEquals(getDigitalAddressInfoCamereOKDto.getCorrelationId(), "correlationId");
    }
}

