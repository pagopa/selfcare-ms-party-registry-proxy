package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.api.OpenDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.OpenDataRestClient;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

abstract class OpenDataConnectorTemplateTest {

    @Test
    void getInstitutions() throws IOException {
        // given
        final String resourceLocation = "classpath:institutions-open-data.csv";
        final String csvContent = new String(Files.readAllBytes(ResourceUtils.getFile(resourceLocation).toPath()));
        when(getOpenDataRestClientMock().retrieveInstitutions())
                .thenReturn(csvContent);
        // when
        final List<? extends Institution> institutions = getOpenDataConnector().getInstitutions();
        // then
        assertNotNull(institutions);
        assertEquals(1, institutions.size());
        assertNotNull(institutions.get(0).getId());
        assertNull(institutions.get(0).getO());
        assertNull(institutions.get(0).getOu());
        assertNull(institutions.get(0).getAoo());
        assertNotNull(institutions.get(0).getOriginId());
        assertNotNull(institutions.get(0).getTaxCode());
        assertNotNull(institutions.get(0).getCategory());
        assertNotNull(institutions.get(0).getDescription());
        assertNotNull(institutions.get(0).getDigitalAddress());
        assertNotNull(institutions.get(0).getAddress());
        assertNotNull(institutions.get(0).getZipCode());
        assertNotNull(institutions.get(0).getOrigin());
        verify(getOpenDataRestClientMock(), times(1))
                .retrieveInstitutions();
        verifyNoMoreInteractions(getOpenDataRestClientMock());
    }


    @Test
    void getCategories() throws IOException {
        // given
        final String resourceLocation = "classpath:categories-open-data.csv";
        final String csvContent = new String(Files.readAllBytes(ResourceUtils.getFile(resourceLocation).toPath()));
        when(getOpenDataRestClientMock().retrieveCategories())
                .thenReturn(csvContent);
        // when
        final List<? extends Category> categories = getOpenDataConnector().getCategories();
        // then
        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertNotNull(categories.get(0).getId());
        assertNotNull(categories.get(0).getCode());
        assertNotNull(categories.get(0).getName());
        assertNotNull(categories.get(0).getKind());
        assertNotNull(categories.get(0).getOrigin());
        verify(getOpenDataRestClientMock(), times(1))
                .retrieveCategories();
        verifyNoMoreInteractions(getOpenDataRestClientMock());
    }


    protected abstract OpenDataConnector getOpenDataConnector();

    protected abstract OpenDataRestClient getOpenDataRestClientMock();

}