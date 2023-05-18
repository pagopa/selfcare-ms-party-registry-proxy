package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryFilter;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;
import it.pagopa.selfcare.party.registry_proxy.core.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.core.exception.TooManyResourceFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.party.registry_proxy.connector.model.UO.createId;

@Slf4j
@Service
class UOServiceImpl implements UOService {

    private final IndexSearchService<UO> indexSearchService;


    @Autowired
    UOServiceImpl(IndexSearchService<UO> indexSearchService) {
        log.trace("Initializing {}", UOServiceImpl.class.getSimpleName());
        this.indexSearchService = indexSearchService;
    }


    @Override
    public QueryResult<UO> search(Optional<Origin> origin, int page, int limit) {
        log.trace("search start");
        log.debug("search origin = {}, page = {}, limit = {}", origin, page, limit);
        final QueryResult<UO> queryResult;
        if (origin.isPresent()) {
            final QueryFilter queryFilter = new QueryFilter();
            queryFilter.setField(UO.Field.ORIGIN);
            queryFilter.setValue(origin.get().toString());
            queryResult = indexSearchService.findAll(page, limit, queryFilter);
        } else {
            queryResult = indexSearchService.findAll(page, limit);
        }
        log.debug("search result = {}", queryResult);
        log.trace("search end");
        return queryResult;
    }


    @Override
    public UO findById(String id, Origin origin) {
        log.trace("findById start");
        log.debug("findById id = {}, origin = {}", id, origin);
        if (Origin.INFOCAMERE.equals(origin)) {
            throw new RuntimeException("Data source not found");//FIXME: there is no UO...choose the right exception to throw
        } else {
            final List<UO> uos = indexSearchService.findById(UO.Field.ID, createId(origin,id));
            if (uos.isEmpty()) {
                throw new ResourceNotFoundException();
            } else if (uos.size() > 1) {
                throw new TooManyResourceFoundException();
            } else {
                final UO uo = uos.get(0);
                log.debug("findById result = {}", uo);
                log.trace("findById end");
                return uo;
            }
        }
    }

}
