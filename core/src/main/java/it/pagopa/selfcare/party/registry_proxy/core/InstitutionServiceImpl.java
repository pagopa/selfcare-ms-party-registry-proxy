package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution.Field;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.core.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.core.exception.TooManyResourceFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
class InstitutionServiceImpl implements InstitutionService {

    private final IndexSearchService<Institution> indexSearchService;


    @Autowired
    InstitutionServiceImpl(IndexSearchService<Institution> indexSearchService) {
        log.trace("Initializing {}", InstitutionServiceImpl.class.getSimpleName());
        this.indexSearchService = indexSearchService;
    }


    @Override
    public QueryResult<Institution> search(Optional<String> searchText, int page, int limit) {
        log.trace("search start");
        log.debug("search searchText = {}, page = {}, limit = {}", searchText, page, limit);
        final QueryResult<Institution> queryResult = searchText.map(text -> indexSearchService.fullTextSearch(Field.DESCRIPTION, text, page, limit))
                .orElseGet(() -> indexSearchService.findAll(page, limit));
        log.debug("search result = {}", queryResult);
        log.trace("search end");
        return queryResult;
    }

    @Override
    public QueryResult<Institution> search(Optional<String> searchText, String categories, int page, int limit) {
        log.trace("search start");
        log.debug("search searchText = {}, categories = {}, page = {}, limit = {}", searchText, categories, page, limit);

        final QueryResult<Institution> queryResult = searchText.map(text -> indexSearchService.fullTextSearch(Field.DESCRIPTION, searchText.orElseThrow(), Field.CATEGORY, categories, page, limit))
                .orElseGet(() -> indexSearchService.findAll(page, limit));
        

        log.debug("search result = {}", queryResult);
        log.trace("search end");
        return queryResult;
    }


    @Override
    public Institution findById(String id, Optional<Origin> origin, List<String> categories) {
        log.trace("findById start");
        log.debug("findById id = {}, origin = {}", id, origin);
        if (origin.map(Origin.INFOCAMERE::equals).orElse(false)) {
            throw new RuntimeException("Not implemented yet");//TODO: onboarding privati
        } else {
            final Supplier<List<Institution>> institutionsSupplier = () -> indexSearchService.findById(Field.ID, id);
            final List<Institution> institutions = origin.map(orig -> institutionsSupplier.get().stream()
                            .filter(institution -> institution.getOrigin().equals(orig) &&
                                     categories.contains(institution.getCategory())
                                    )
                    .collect(Collectors.toList()))
                    .orElseGet(institutionsSupplier);

            if (institutions.isEmpty()) {
                throw new ResourceNotFoundException();
            } else if (institutions.size() > 1) {
                throw new TooManyResourceFoundException();
            } else {
                final Institution institution = institutions.get(0);
                log.debug("findById result = {}", institution);
                log.trace("findById end");
                return institution;
            }
        }
    }

}
