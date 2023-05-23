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
class AOOServiceImplTest {

    @Mock
    private IndexSearchService<AOO> indexSearchService;

    @InjectMocks
    private AOOServiceImpl aooService;


    @Test
    void search_emptyOrigin() {
        // given
        final int page = 0;
        final int limit = 0;

        final DummyAOOQueryResult queryResultMock = new DummyAOOQueryResult();
        when(indexSearchService.findAll(anyInt(), anyInt(), anyString()))
                .thenReturn(queryResultMock);
        // when
        final QueryResult<AOO> queryResult = aooService.findAll(page, limit);
        // then
        assertSame(queryResultMock, queryResult);
        verify(indexSearchService, times(1))
                .findAll(page, limit, Entity.AOO.toString());
        verifyNoMoreInteractions(indexSearchService);
    }


    @Test
    void search_notEmptyOrigin() {
        // given
        final int page = 0;
        final int limit = 0;
        final DummyAOOQueryResult queryResultMock = new DummyAOOQueryResult();
        when(indexSearchService.findAll(anyInt(), anyInt(), any(), any()))
                .thenReturn(queryResultMock);
        // when
        final QueryResult<AOO> queryResult = aooService.findAll(page, limit);
        // then
        assertSame(queryResultMock, queryResult);
    }

    @Test
    void findById_ipa() {
        // given
        final String id = "pippo";
        // when
        final Executable executable = () -> aooService.findByUnicode(id);
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
        final Executable executable = () -> aooService.findByUnicode(id);
        // then
        assertThrows(ResourceNotFoundException.class, executable);
    }


    @Test
    void findById_TooManyResourceFound() {
        // given
        final String id = "pippo";
        final DummyAOO aoo = new DummyAOO();
        when(indexSearchService.findById(any(), anyString()))
                .thenReturn(List.of(aoo, aoo));
        // when
        final Executable executable = () -> aooService.findByUnicode(id);
        // then
        assertThrows(TooManyResourceFoundException.class, executable);
    }


    @Test
    void findById_found() {
        // given
        final String id = "pippo";
        final DummyAOO AOO = new DummyAOO();
        when(indexSearchService.findById(any(), anyString()))
                .thenReturn(List.of(AOO));
        // when
        final AOO result = aooService.findByUnicode(id);
        // then
        assertSame(AOO, result);
    }

}