package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.FullTextQueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution.Field;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
class InstitutionServiceImpl implements InstitutionService {

    private final IndexSearchService<Institution> indexSearchService;


    @Autowired
    InstitutionServiceImpl(IndexSearchService<Institution> indexSearchService) {
        this.indexSearchService = indexSearchService;
    }


    @Override
    public FullTextQueryResult<Institution> search(String searchText, int page, int limit) {
        return indexSearchService.fullTextSearch(Field.DESCRIPTION, searchText, page, limit);
    }


    @Override
    public Institution findById(String id, Optional<Origin> origin) {
        if (origin.map(Origin.INFOCAMERE::equals).orElse(false)) {
            throw new RuntimeException("Not implemented yet");
        } else {
            final List<Institution> institutions = indexSearchService.search(Field.ID, id);
            if (institutions.isEmpty()) {
                throw new RuntimeException();//FIXME use ResourceNotFound
            } else if (institutions.size() > 1) {
                throw new RuntimeException();//FIXME use TooManyRecordsFound
            } else {
                return institutions.get(0);
            }
        }
    }

}
