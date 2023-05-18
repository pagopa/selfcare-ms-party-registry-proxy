package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryFilter;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.core.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.core.exception.TooManyResourceFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.party.registry_proxy.connector.model.AOO.createId;

@Slf4j
@Service
class AOOServiceImpl implements AOOService {

    private final IndexSearchService<AOO> indexSearchService;


    @Autowired
    AOOServiceImpl(IndexSearchService<AOO> indexSearchService) {
        log.trace("Initializing {}", AOOServiceImpl.class.getSimpleName());
        this.indexSearchService = indexSearchService;
    }


    @Override
    public QueryResult<AOO> search(Optional<Origin> origin, int page, int limit) {
        log.trace("search start");
        log.debug("search origin = {}, page = {}, limit = {}", origin, page, limit);
        final QueryResult<AOO> queryResult;
        if (origin.isPresent()) {
            final QueryFilter queryFilter = new QueryFilter();
            queryFilter.setField(AOO.Field.ORIGIN);
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
    public AOO findById(String id, Origin origin) {
        log.trace("findById start");
        log.debug("findById id = {}, origin = {}", id, origin);
        if (Origin.INFOCAMERE.equals(origin)) {
            throw new RuntimeException("Data source not found");//FIXME: there is no AOO...choose the right exception to throw
        } else {
            final List<AOO> aoos = indexSearchService.findById(AOO.Field.ID, createId(origin,id));
            if (aoos.isEmpty()) {
                throw new ResourceNotFoundException();
            } else if (aoos.size() > 1) {
                throw new TooManyResourceFoundException();
            } else {
                final AOO aoo = aoos.get(0);
                log.debug("findById result = {}", aoo);
                log.trace("findById end");
                return aoo;
            }
        }
    }

}
