package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.api.OpenDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {OpenDataLoader.class})
class OpenDataLoaderTest {

    @MockBean
    private OpenDataConnector openDataConnector;

    @MockBean
    private IndexWriterService<Institution> institutionIndexWriterService;

    @MockBean
    private IndexWriterService<Category> categoryIndexWriterService;

    @Autowired
    private OpenDataLoader openDataLoader;


    @Test
    void run() {
        // given
        final List institutions = List.of();
        when(openDataConnector.getInstitutions())
                .thenReturn(institutions);
        final List categories = List.of();
        when(openDataConnector.getCategories())
                .thenReturn(categories);
        // when
        openDataLoader.run();
        // then
        verify(openDataConnector, times(1))
                .getInstitutions();
        verify(institutionIndexWriterService, times(1))
                .adds(institutions);
        verify(openDataConnector, times(1))
                .getCategories();
        verify(categoryIndexWriterService, times(1))
                .adds(categories);
        verifyNoMoreInteractions(openDataConnector, institutionIndexWriterService, categoryIndexWriterService);
    }

}