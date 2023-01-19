package it.pagopa.selfcare.party.registry_proxy.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereLegalAddress;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereLocationAddress;
import it.pagopa.selfcare.party.registry_proxy.core.InfoCamereBatchRequestService;
import it.pagopa.selfcare.party.registry_proxy.core.InfoCamereService;
import it.pagopa.selfcare.party.registry_proxy.web.model.*;
import it.pagopa.selfcare.party.registry_proxy.web.model.GetInstitutionsByLegalDto;
import it.pagopa.selfcare.party.registry_proxy.web.model.GetInstitutionsByLegalFilterDto;

import java.time.LocalDate;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class InfoCamereControllerTest {
    @MockBean
    private InfoCamereBatchRequestService infoCamereBatchRequestService;

    @InjectMocks
    private InfoCamereController infoCamereController;
    @Mock
    private InfoCamereService infoCamereService;

    /**
     * Method under test: {@link InfoCamereController#institutionsByLegalTaxId(GetInstitutionsByLegalDto)}
     */
    @Test
    void testInstitutionsByLegalTaxId() throws Exception {
        Businesses businesses = new Businesses();
        businesses.setBusinesses(new ArrayList<>());
        businesses.setLegalTaxId("42");
        businesses.setRequestDateTime("2020-03-01");
        when(infoCamereService.institutionsByLegalTaxId(any())).thenReturn(businesses);

        GetInstitutionsByLegalFilterDto getInstitutionsByLegalFilterDto = new GetInstitutionsByLegalFilterDto();
        getInstitutionsByLegalFilterDto.setLegalTaxId("42");

        GetInstitutionsByLegalDto getInstitutionsByLegalDto = new GetInstitutionsByLegalDto();
        getInstitutionsByLegalDto.setFilter(getInstitutionsByLegalFilterDto);
        String content = (new ObjectMapper()).writeValueAsString(getInstitutionsByLegalDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/info-camere/institutions")
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
        assertEquals(getDigitalAddressInfoCamereOKDto.getCorrelationId(), "id");
    }

    /**
     * Method under test: {@link InfoCamereController#legalAddressByTaxId(ProfessionalAddressRequestBodyDto)}
     */
    @Test
    void testLegalAddressByTaxId() throws Exception {
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
        when(infoCamereService.legalAddressByTaxId(any())).thenReturn(infoCamereLegalAddress);

        ProfessionalAddressRequestBodyFilterDto professionalAddressRequestBodyFilterDto = new ProfessionalAddressRequestBodyFilterDto();
        professionalAddressRequestBodyFilterDto.setTaxId("42");

        ProfessionalAddressRequestBodyDto professionalAddressRequestBodyDto = new ProfessionalAddressRequestBodyDto();
        professionalAddressRequestBodyDto.setFilter(professionalAddressRequestBodyFilterDto);
        String content = (new ObjectMapper()).writeValueAsString(professionalAddressRequestBodyDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/info-camere/legal-address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(infoCamereController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"dateTimeExtraction\":0,\"taxId\":\"42\",\"legalAddress\":{\"description\":\"42 Main St\",\"municipality\":"
                                        + "\"Municipality\",\"province\":\"Province\",\"address\":\"Toponym Street 42\",\"zip\":\"Postal Code\"}}"));
    }
}

