package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.DummyPDNDQueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Entity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station.Field;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PDNDServiceImplTest {

    @Mock
    private IndexSearchService<Station> indexSearchService;

    @InjectMocks
    private PDNDServiceImpl pdndService;

    @Test
    void search_emptySearchText() {
        // given
        final Optional<String> searchText = Optional.empty();
        final int page = 0;
        final int limit = 0;

        final DummyPDNDQueryResult queryResultMock = new DummyPDNDQueryResult();
        when(indexSearchService.findAll(anyInt(), anyInt(), anyString()))
                .thenReturn(queryResultMock);
        // when
        final QueryResult<Station> queryResult = pdndService.search(searchText, page, limit);
        // then
        assertSame(queryResultMock, queryResult);
        verify(indexSearchService, times(1))
                .findAll(page, limit, Entity.STATION.toString());
        verifyNoMoreInteractions(indexSearchService);
    }

    @Test
    void search_notEmptySearchText() {
        // given
        final Optional<String> searchText = Optional.of("pippo");
        final int page = 0;
        final int limit = 0;

        final DummyPDNDQueryResult queryResultMock = new DummyPDNDQueryResult();
        when(indexSearchService.fullTextSearch(any(), anyString(), anyInt(), anyInt()))
                .thenReturn(queryResultMock);
        // when
        final QueryResult<Station> queryResult = pdndService.search(searchText, page, limit);
        // then
        assertSame(queryResultMock, queryResult);
        verify(indexSearchService, times(1))
                .fullTextSearch(Field.DESCRIPTION, searchText.get(), page, limit);
        verifyNoMoreInteractions(indexSearchService);
    }
}