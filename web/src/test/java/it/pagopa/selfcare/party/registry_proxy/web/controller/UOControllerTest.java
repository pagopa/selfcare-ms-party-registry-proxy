package it.pagopa.selfcare.party.registry_proxy.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.core.UOService;
import it.pagopa.selfcare.party.registry_proxy.web.config.WebTestConfig;
import it.pagopa.selfcare.party.registry_proxy.web.handler.PartyRegistryProxyExceptionHandler;
import it.pagopa.selfcare.party.registry_proxy.web.model.DummyUO;
import it.pagopa.selfcare.party.registry_proxy.web.model.DummyUOQueryResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {UOController.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {UOController.class, WebTestConfig.class, PartyRegistryProxyExceptionHandler.class})
class UOControllerTest {

    private static final String BASE_URL = "/v1";

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper mapper;

    @MockBean
    private UOService uoServiceMock;


    @Test
    void findUOs_defaultInputParams() throws Exception {
        // given
        when(uoServiceMock.search(any(), anyInt(), anyInt()))
                .thenReturn(new DummyUOQueryResult());
        // when
        mvc.perform(MockMvcRequestBuilders
                .get(BASE_URL + "/uos")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", notNullValue()));
        // then
        verify(uoServiceMock, times(1))
                .search(Optional.empty(), 1, 10);
        verifyNoMoreInteractions(uoServiceMock);
    }


    @Test
    void findUOs() throws Exception {
        // given
        final Origin origin = Origin.MOCK;
        final String page = "2";
        final String limit = "2";
        when(uoServiceMock.search(any(), anyInt(), anyInt()))
                .thenReturn(new DummyUOQueryResult());
        // when
        mvc.perform(MockMvcRequestBuilders
                .get(BASE_URL + "/uos")
                .queryParam("origin", origin.toString())
                .queryParam("page", page)
                .queryParam("limit", limit)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", notNullValue()));
        // then
        verify(uoServiceMock, times(1))
                .search(Optional.of(origin), Integer.parseInt(page), Integer.parseInt(limit));
        verifyNoMoreInteractions(uoServiceMock);
    }

    @Test
    void findUO() throws Exception {
        // given
        final Origin origin = Origin.IPA;
        final String code = "code";
        when(uoServiceMock.findById(any(), any()))
                .thenReturn(mockInstance(new DummyUO()));
        // when
        mvc.perform(MockMvcRequestBuilders
                .get(BASE_URL + "/origins/{origin}/uos/{codiceUniUo}", origin, code)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        // then
        verify(uoServiceMock, times(1))
                .findById(code, origin);
        verifyNoMoreInteractions(uoServiceMock);
    }

}