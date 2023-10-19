package it.pagopa.selfcare.party.registry_proxy.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.core.IvassService;
import it.pagopa.selfcare.party.registry_proxy.web.config.WebTestConfig;
import it.pagopa.selfcare.party.registry_proxy.web.handler.PartyRegistryProxyExceptionHandler;
import it.pagopa.selfcare.party.registry_proxy.web.model.DummyInsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.InsuranceCompanyMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {IvassController.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {IvassController.class, WebTestConfig.class, PartyRegistryProxyExceptionHandler.class, InsuranceCompanyMapperImpl.class})
class IvassControllerTest {

    private static final String BASE_URL = "/insurance-companies";

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper mapper;

    @MockBean
    private IvassService ivassService;

    @Test
    void findInsurance() throws Exception {
        // given
        final String taxId = "CODE";
        when(ivassService.findByTaxCode(any()))
                .thenReturn(mockInstance(new DummyInsuranceCompany()));
        // when
        mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/{taxId}", taxId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.originId", notNullValue()))
                .andExpect(jsonPath("$.taxCode", notNullValue()))
                .andExpect(jsonPath("$.description", notNullValue()))
                .andExpect(jsonPath("$.digitalAddress", notNullValue()))
                .andExpect(jsonPath("$.address", notNullValue()));
        // then

        verify(ivassService, times(1))
                .findByTaxCode(taxId);
        verifyNoMoreInteractions(ivassService);
    }

    @Test
    void findInsuranceNotFound() throws Exception {
        // given
        final String taxId = "CODE";
        when(ivassService.findByTaxCode(any()))
                .thenThrow(ResourceNotFoundException.class);
        // when
        mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/{taxId}", taxId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
        // then
        verify(ivassService, times(1))
                .findByTaxCode(taxId);
        verifyNoMoreInteractions(ivassService);
    }
}