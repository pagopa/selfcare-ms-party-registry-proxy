package it.pagopa.selfcare.party.registry_proxy.web.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.LegalAddressProfessionalResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.VerifyLegalResponse;
import it.pagopa.selfcare.party.registry_proxy.core.NationalRegistriesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {NationalRegistriesController.class})
@ExtendWith(SpringExtension.class)
class NationalRegistriesControllerTest {
    @Autowired
    private NationalRegistriesController nationalRegistriesController;

    @MockBean
    private NationalRegistriesService nationalRegistriesService;

    /**
     * Method under test: {@link NationalRegistriesController#legalAddress(String)}
     */
    @Test
    void testLegalAddress() throws Exception {
        LegalAddressProfessionalResponse legalAddressProfessionalResponse = new LegalAddressProfessionalResponse();
        legalAddressProfessionalResponse.setAddress("42 Main St");
        legalAddressProfessionalResponse.setDescription("The characteristics of someone or something");
        legalAddressProfessionalResponse.setMunicipality("Municipality");
        legalAddressProfessionalResponse.setProvince("Province");
        legalAddressProfessionalResponse.setZip("21654");
        when(nationalRegistriesService.getLegalAddress((String) any())).thenReturn(legalAddressProfessionalResponse);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/national-registries/legal-address")
                .param("taxId", "foo");
        MockMvcBuilders.standaloneSetup(nationalRegistriesController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"address\":\"42 Main St, Municipality (Province)\",\"zipCode\":\"21654\"}"));
    }

    /**
     * Method under test: {@link NationalRegistriesController#verifyLegal(String, String)}
     */
    @Test
    void testVerifyLegal() throws Exception {
        VerifyLegalResponse verifyLegalResponse = new VerifyLegalResponse();
        verifyLegalResponse.setVerificationResult(true);
        verifyLegalResponse.setVerifyLegalResultCode("Verify Legal Result Code");
        verifyLegalResponse.setVerifyLegalResultDetail("Verify Legal Result Detail");
        when(nationalRegistriesService.verifyLegal((String) any(), (String) any())).thenReturn(verifyLegalResponse);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/national-registries/verify-legal")
                .param("taxId", "foo")
                .param("vatNumber", "foo");
        MockMvcBuilders.standaloneSetup(nationalRegistriesController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"verificationResult\":true,\"resultCode\":\"Verify Legal Result Code\",\"resultDetail\":\"Verify Legal Result"
                                        + " Detail\"}"));
    }

    /**
     * Method under test: {@link NationalRegistriesController#legalAddress(String)}
     */
    @Test
    void testLegalAddress2() throws Exception {
        LegalAddressProfessionalResponse legalAddressProfessionalResponse = new LegalAddressProfessionalResponse();
        legalAddressProfessionalResponse.setAddress("42 Main St");
        legalAddressProfessionalResponse.setDescription("The characteristics of someone or something");
        legalAddressProfessionalResponse.setMunicipality("");
        legalAddressProfessionalResponse.setProvince("Province");
        legalAddressProfessionalResponse.setZip("21654");
        when(nationalRegistriesService.getLegalAddress((String) any())).thenReturn(legalAddressProfessionalResponse);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/national-registries/legal-address")
                .param("taxId", "foo");
        MockMvcBuilders.standaloneSetup(nationalRegistriesController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"address\":\"42 Main St (Province)\",\"zipCode\":\"21654\"}"));
    }

    /**
     * Method under test: {@link NationalRegistriesController#legalAddress(String)}
     */
    @Test
    void testLegalAddress3() throws Exception {
        LegalAddressProfessionalResponse legalAddressProfessionalResponse = new LegalAddressProfessionalResponse();
        legalAddressProfessionalResponse.setAddress("42 Main St");
        legalAddressProfessionalResponse.setDescription("The characteristics of someone or something");
        legalAddressProfessionalResponse.setMunicipality("Municipality");
        legalAddressProfessionalResponse.setProvince("");
        legalAddressProfessionalResponse.setZip("21654");
        when(nationalRegistriesService.getLegalAddress((String) any())).thenReturn(legalAddressProfessionalResponse);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/national-registries/legal-address")
                .param("taxId", "foo");
        MockMvcBuilders.standaloneSetup(nationalRegistriesController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"address\":\"42 Main St, Municipality\",\"zipCode\":\"21654\"}"));
    }
}

