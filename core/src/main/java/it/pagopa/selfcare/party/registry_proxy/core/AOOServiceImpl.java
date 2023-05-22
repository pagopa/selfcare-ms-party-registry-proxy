package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Entity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.core.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.core.exception.TooManyResourceFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
class AOOServiceImpl implements AOOService {

    private final IndexSearchService<AOO> indexSearchService;

    AOOServiceImpl(IndexSearchService<AOO> indexSearchService) {
        log.trace("Initializing {}", AOOServiceImpl.class.getSimpleName());
        this.indexSearchService = indexSearchService;
    }

    @Override
    public QueryResult<AOO> findAll(int page, int limit) {
        log.trace("find all AOO start");
        log.debug("find all AOO all with page = {}, limit = {}", page, limit);
        final QueryResult<AOO> queryResult;
        queryResult = indexSearchService.findAll(page, limit, Entity.AOO.toString());
        log.debug("find all AOO result = {}", queryResult);
        log.trace("find all AOO end");
        return queryResult;
    }


    @Override
    public AOO findByUnicode(String codiceUniAOO) {
        log.trace("find AOO by CodiceUniAOO start");
        log.debug("find AOO by CodiceUniAOO = {}", codiceUniAOO);
        final List<AOO> aooList = indexSearchService.findById(AOO.Field.CODICE_UNI_AOO, codiceUniAOO);
        if (aooList.isEmpty()) {
            throw new ResourceNotFoundException();
        } else if (aooList.size() > 1) {
            throw new TooManyResourceFoundException();
        }
        final AOO aoo = aooList.get(0);
        log.debug("find AOO by CodiceUniAOO result = {}", aoo);
        log.trace("find AOO by CodiceUniAOO end");
        return aoo;
    }
}
