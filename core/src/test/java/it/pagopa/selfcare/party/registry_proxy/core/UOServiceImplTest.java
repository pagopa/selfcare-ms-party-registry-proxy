package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.*;
import it.pagopa.selfcare.party.registry_proxy.core.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.core.exception.TooManyResourceFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UOServiceImplTest {

    @Mock
    private IndexSearchService<UO> indexSearchService;

    @InjectMocks
    private UOServiceImpl uoService;


    @Test
    void search_emptyOrigin() {
        // given
        final Optional<Origin> origin = Optional.empty();
        final int page = 0;
        final int limit = 0;

        final DummyUOQueryResult queryResultMock = new DummyUOQueryResult();
        when(indexSearchService.findAll(anyInt(), anyInt()))
                .thenReturn(queryResultMock);
        // when
        final QueryResult<UO> queryResult = uoService.search(origin, page, limit);
        // then
        assertSame(queryResultMock, queryResult);
        verify(indexSearchService, times(1))
                .findAll(page, limit);
        verifyNoMoreInteractions(indexSearchService);
    }


    @Test
    void search_notEmptyOrigin() {
        // given
        final Optional<Origin> origin = Optional.of(Origin.IPA);
        final int page = 0;
        final int limit = 0;
        final DummyUOQueryResult queryResultMock = new DummyUOQueryResult();
        when(indexSearchService.findAll(anyInt(), anyInt(), any()))
                .thenReturn(queryResultMock);
        // when
        final QueryResult<UO> queryResult = uoService.search(origin, page, limit);
        // then
        assertSame(queryResultMock, queryResult);
        final ArgumentCaptor<QueryFilter> queryFilterArgumentCaptor = ArgumentCaptor.forClass(QueryFilter.class);
        verify(indexSearchService, times(1))
                .findAll(eq(page), eq(limit), queryFilterArgumentCaptor.capture());
        final QueryFilter queryFilter = queryFilterArgumentCaptor.getValue();
        assertEquals(origin.get().toString(), queryFilter.getValue());
        verifyNoMoreInteractions(indexSearchService);
    }


    private static String createId(Origin origin, String code) {
        return origin + "_" + code;
    }


    @Test
    void findById_ipa() {
        // given
        final String id = "pippo";
        final Origin origin = Origin.IPA;
        // when
        final Executable executable = () -> uoService.findById(id, origin);
        // then
        assertThrows(RuntimeException.class, executable);
    }


    @Test
    void findById_ResourceNotFound() {
        // given
        final String id = "pippo";
        final Origin origin = Origin.MOCK;
        when(indexSearchService.findById(any(), anyString()))
                .thenReturn(List.of());
        // when
        final Executable executable = () -> uoService.findById(id, origin);
        // then
        assertThrows(ResourceNotFoundException.class, executable);
    }


    @Test
    void findById_TooManyResourceFound() {
        // given
        final String id = "pippo";
        final Origin origin = Origin.MOCK;
        final DummyUO UO = new DummyUO();
        when(indexSearchService.findById(any(), anyString()))
                .thenReturn(List.of(UO, UO));
        // when
        final Executable executable = () -> uoService.findById(id, origin);
        // then
        assertThrows(TooManyResourceFoundException.class, executable);
    }


    @Test
    void findById_found() {
        // given
        final String id = "pippo";
        final Origin origin = Origin.IPA;
        final DummyUO UO = new DummyUO();
        when(indexSearchService.findById(any(), anyString()))
                .thenReturn(List.of(UO));
        // when
        final UO result = uoService.findById(id, origin);
        // then
        assertSame(UO, result);
    }

}