package it.pagopa.selfcare.party.registry_proxy.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;
import it.pagopa.selfcare.party.registry_proxy.core.InfoCamereService;

import it.pagopa.selfcare.party.registry_proxy.web.model.GetInstitutionsByLegalDto;
import it.pagopa.selfcare.party.registry_proxy.web.model.GetInstitutionsByLegalFilterDto;


import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class InfoCamereControllerTest {

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
        businesses.setLegalTaxId("CIACIA80A01H501X");
        businesses.setRequestDateTime("2020-03-01");
        when(infoCamereService.institutionsByLegalTaxId(any())).thenReturn(businesses);

        GetInstitutionsByLegalFilterDto getInstitutionsByLegalFilterDto = new GetInstitutionsByLegalFilterDto();
        getInstitutionsByLegalFilterDto.setLegalTaxId("CIACIA80A01H501X");

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
                        .string("{\"requestDateTime\":\"2020-03-01\",\"legalTaxId\":\"CIACIA80A01H501X\",\"businesses\":[]}"));
    }
}

