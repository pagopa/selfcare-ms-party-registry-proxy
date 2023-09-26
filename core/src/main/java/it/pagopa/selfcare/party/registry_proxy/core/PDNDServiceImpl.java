package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Entity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PDNDServiceImpl implements PDNDService {
    private final IndexSearchService<Station> indexSearchService;

    @Autowired
    PDNDServiceImpl(IndexSearchService<Station> indexSearchService) {
        log.trace("Initializing {}", PDNDServiceImpl.class.getSimpleName());
        this.indexSearchService = indexSearchService;
    }

    @Override
    public QueryResult<Station> search(Optional<String> searchText, int page, int limit) {
        log.trace("search start");
        log.debug("search searchText = {}, page = {}, limit = {}", searchText, page, limit);
        final QueryResult<Station> queryResult = searchText.map(text -> indexSearchService.fullTextSearch(Station.Field.DESCRIPTION, text, page, limit))
                .orElseGet(() -> indexSearchService.findAll(page, limit, Entity.STATION.toString()));
        log.debug("search result = {}", queryResult);
        log.trace("search end");
        return queryResult;
    }

}
