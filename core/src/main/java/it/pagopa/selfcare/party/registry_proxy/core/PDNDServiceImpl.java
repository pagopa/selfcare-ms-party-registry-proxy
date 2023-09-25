package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Entity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.PDND;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PDNDServiceImpl implements PDNDService {
    private final IndexSearchService<PDND> indexSearchService;

    @Autowired
    PDNDServiceImpl(IndexSearchService<PDND> indexSearchService) {
        log.trace("Initializing {}", PDNDServiceImpl.class.getSimpleName());
        this.indexSearchService = indexSearchService;
    }

    @Override
    public QueryResult<PDND> search(Optional<String> searchText, int page, int limit) {
        log.trace("search start");
        log.debug("search searchText = {}, page = {}, limit = {}", searchText, page, limit);
        final QueryResult<PDND> queryResult = searchText.map(text -> indexSearchService.fullTextSearch(PDND.Field.DESCRIPTION, text, page, limit))
                .orElseGet(() -> indexSearchService.findAll(page, limit, Entity.PDND.toString()));
        log.debug("search result = {}", queryResult);
        log.trace("search end");
        return queryResult;
    }

}
