package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.*;
import it.pagopa.selfcare.party.registry_proxy.core.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.core.exception.TooManyResourceFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        final int page = 0;
        final int limit = 0;

        final DummyUOQueryResult queryResultMock = new DummyUOQueryResult();
        when(indexSearchService.findAll(anyInt(), anyInt(), anyString()))
                .thenReturn(queryResultMock);
        // when
        final QueryResult<UO> queryResult = uoService.findAll(page, limit);
        // then
        assertSame(queryResultMock, queryResult);
        verify(indexSearchService, times(1))
                .findAll(page, limit, Entity.UO.toString());
        verifyNoMoreInteractions(indexSearchService);
    }


    @Test
    void search_notEmptyOrigin() {
        final int page = 0;
        final int limit = 0;
        final DummyUOQueryResult queryResultMock = new DummyUOQueryResult();
        when(indexSearchService.findAll(anyInt(), anyInt(), any(), any()))
                .thenReturn(queryResultMock);
        // when
        final QueryResult<UO> queryResult = uoService.findAll(page, limit);
        // then
        assertSame(queryResultMock, queryResult);
    }

    @Test
    void findById_ipa() {
        // given
        final String id = "pippo";
        // when
        final Executable executable = () -> uoService.findByUnicode(id);
        // then
        assertThrows(RuntimeException.class, executable);
    }


    @Test
    void findById_ResourceNotFound() {
        // given
        final String id = "pippo";
        when(indexSearchService.findById(any(), anyString()))
                .thenReturn(List.of());
        // when
        final Executable executable = () -> uoService.findByUnicode(id);
        // then
        assertThrows(ResourceNotFoundException.class, executable);
    }


    @Test
    void findById_TooManyResourceFound() {
        // given
        final String id = "pippo";
        final DummyUO UO = new DummyUO();
        when(indexSearchService.findById(any(), anyString()))
                .thenReturn(List.of(UO, UO));
        // when
        final Executable executable = () -> uoService.findByUnicode(id);
        // then
        assertThrows(TooManyResourceFoundException.class, executable);
    }


    @Test
    void findById_found() {
        // given
        final String id = "pippo";
        final DummyUO UO = new DummyUO();
        when(indexSearchService.findById(any(), anyString()))
                .thenReturn(List.of(UO));
        // when
        final UO result = uoService.findByUnicode(id);
        // then
        assertSame(UO, result);
    }

}