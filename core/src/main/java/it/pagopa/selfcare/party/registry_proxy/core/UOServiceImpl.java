package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Entity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;
import it.pagopa.selfcare.party.registry_proxy.core.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.core.exception.TooManyResourceFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
class UOServiceImpl implements UOService {

    private final IndexSearchService<UO> indexSearchService;
    private final InstitutionService institutionService;

    UOServiceImpl(IndexSearchService<UO> indexSearchService,
                  InstitutionService institutionService) {
        log.trace("Initializing {}", UOServiceImpl.class.getSimpleName());
        this.indexSearchService = indexSearchService;
        this.institutionService = institutionService;
    }


    @Override
    public QueryResult<UO> findAll(int page, int limit) {
        log.trace("find all UO start");
        log.debug("find all UO, page = {}, limit = {}", page, limit);
        final QueryResult<UO> queryResult;

        queryResult = indexSearchService.findAll(page, limit, Entity.UO.toString());

        log.debug("find all UO result = {}", queryResult);
        log.trace("find all UO end");
        return queryResult;
    }


    @Override
    public UO findByUnicode(String codiceUniUO, List<String> categoriesList) {
        log.trace("find UO By CodiceUniUO start");
        log.debug("find UO By CodiceUniUO = {} - categoriesList = {}", codiceUniUO.toUpperCase(), categoriesList);
        final List<UO> uoList = indexSearchService.findById(UO.Field.CODICE_UNI_UO, codiceUniUO.toUpperCase());
        if (uoList.isEmpty()) {
            throw new ResourceNotFoundException();
        } else if (uoList.size() > 1) {
            throw new TooManyResourceFoundException();
        }
        final UO uo = uoList.get(0);
        if(!categoriesList.isEmpty()){
            Institution institution = institutionService.findById(uo.getCodiceFiscaleEnte(), categoriesList);
        }
        log.debug("find UO By CodiceUniUO result = {}", uo);
        log.trace("find UO By CodiceUniUO end");
        return uo;
    }
}
