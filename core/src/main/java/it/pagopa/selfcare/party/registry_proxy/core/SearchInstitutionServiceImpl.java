package it.pagopa.selfcare.party.registry_proxy.core;

import static it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.Const.INDEX_API_VERSION;
import static it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.Const.IPA_INDEX_NAME;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.api.SearchServiceConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.connector.model.*;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.OpenDataRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.IPAOpenDataInstitution;
import it.pagopa.selfcare.party.registry_proxy.core.exception.TooManyResourceFoundException;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class SearchInstitutionServiceImpl implements SearchInstitutionService {

    private final IndexSearchService<Institution> indexSearchService;
    private final OpenDataRestClient openDataRestClient;
    private final IndexWriterService<Institution> institutionIndexWriterService;
    private final SearchServiceConnector searchServiceConnector;

    @Autowired
    SearchInstitutionServiceImpl(IndexSearchService<Institution> indexSearchService, OpenDataRestClient openDataRestClient,
                                 IndexWriterService<Institution> institutionIndexWriterService,
                                 SearchServiceConnector searchServiceConnector) {
        this.openDataRestClient = openDataRestClient;
        this.institutionIndexWriterService = institutionIndexWriterService;
        log.trace("Initializing {}", SearchInstitutionServiceImpl.class.getSimpleName());
        this.indexSearchService = indexSearchService;
        this.searchServiceConnector = searchServiceConnector;
    }

    @Override
    public List<SearchServiceInstitutionIPA> search(Optional<String> searchText, int page, int limit) {
        log.trace("search start");
        log.debug("search searchText = {}, page = {}, limit = {}", searchText, page, limit);
        List<SearchServiceInstitutionIPA> institutions = searchServiceConnector.searchInstitutionFromIPA(searchText.get(), buildFilter(null), limit, page);
        log.debug("search result = {}", institutions);
        log.trace("search end");
        return institutions;
    }

    @Override
    public List<SearchServiceInstitutionIPA> search(Optional<String> searchText, String categories, int page, int limit) {
        log.trace("search start");
        log.debug("search searchText = {}, categories = {}, page = {}, limit = {}", searchText, categories, page, limit);
        List<SearchServiceInstitutionIPA> institutions = searchServiceConnector.searchInstitutionFromIPA(searchText.get(), buildFilter(Arrays.asList(categories.split(","))), limit, page);
        log.debug("search result = {}", institutions);
        log.trace("search end");
        return institutions;
    }

    @Override
    public SearchServiceInstitutionIPA findById(String id, Optional<Origin> origin, List<String> categories) {
        log.trace("findById start");
        log.debug("findById id = {}, origin = {}", id, origin);
        if (origin.map(Origin.INFOCAMERE::equals).orElse(false)) {
            throw new RuntimeException("Not implemented yet");//TODO: onboarding privati
        } else {
            final List<SearchServiceInstitutionIPA> list = searchServiceConnector.findInstitutionIPAById(id, IPA_INDEX_NAME);

            Origin orig = origin.get();
            List<SearchServiceInstitutionIPA> institutions = list.stream()
                    .filter(institution -> institution.getOrigin().equals(orig.name()) &&
                            (categories.isEmpty() || categories.contains(institution.getCategory())))
                    .toList();

            if (institutions.isEmpty()) {
                throw new ResourceNotFoundException();
            } else if (institutions.size() > 1) {
                throw new TooManyResourceFoundException();
            }
            final SearchServiceInstitutionIPA institution = institutions.get(0);
            log.debug("findById result = {}", institution);
            log.trace("findById end");
            return institution;
        }
    }

    @Scheduled(cron = "0 0 2 * * *")
    void updateInstitutionsIndex() {
        log.trace("start update Institutions IPA index");
        List<Institution> institutions = getInstitutions();
        if (!institutions.isEmpty()) {
            searchServiceConnector.deleteIndex(IPA_INDEX_NAME, INDEX_API_VERSION);
            searchServiceConnector.indexInstitutionsIPA(institutions);
        }
        log.trace("updated Institutions IPA index end");
    }

    private List<Institution> getInstitutions() {
        log.trace("getInstitutions start");
        List<Institution> institutions;
        final String csv = openDataRestClient.retrieveInstitutions();

        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csv.getBytes())))) {
            CsvToBean<Institution> csvToBean = new CsvToBeanBuilder<Institution>(reader)
                    .withType(IPAOpenDataInstitution.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            institutions = csvToBean.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.trace("getInstitutions end");
        return institutions;
    }

    public String buildFilter(List<String> categories) {
        StringBuilder filterBuilder = new StringBuilder();

        if (categories != null && !categories.isEmpty() && !categories.contains("all")) {
            return categories.stream()
                    .map(c -> "category eq '" + c + "'")
                    .collect(Collectors.joining(" or "));
        }
        return filterBuilder.toString();
    }
}
