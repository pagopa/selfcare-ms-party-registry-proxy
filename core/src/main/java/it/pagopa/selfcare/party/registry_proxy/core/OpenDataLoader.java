package it.pagopa.selfcare.party.registry_proxy.core;

import static it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.Const.INDEX_API_VERSION;
import static it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.Const.IPA_INDEX_NAME;

import feign.FeignException;
import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.api.IvassDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.OpenDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.SearchServiceConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.*;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OpenDataLoader implements CommandLineRunner {

    private final List<OpenDataConnector> openDataConnectors;
    private final IndexWriterService<Institution> institutionIndexWriterService;
    private final IndexWriterService<Category> categoryIndexWriterService;
    private final IndexWriterService<AOO> aooIndexWriterService;
    private final IndexWriterService<UO> uoIndexWriterService;
    private final IndexWriterService<Station> stationIndexWriterService;
    private final IndexWriterService<InsuranceCompany> ivassIndexWriterService;
    private final ANACService anacService;
    private final IvassDataConnector ivassDataConnector;

    private final SearchServiceConnector searchServiceConnector;

    @Autowired
    public OpenDataLoader(List<OpenDataConnector> openDataConnectors,
                          IndexWriterService<Institution> institutionIndexWriterService,
                          IndexWriterService<Category> categoryIndexWriterService,
                          IndexWriterService<AOO> aooIndexWriterService,
                          IndexWriterService<UO> uoIndexWriterService,
                          IndexWriterService<Station> stationIndexWriterService,
                          IndexWriterService<InsuranceCompany> ivassIndexWriterService,
                          ANACService anacService,
                          IvassDataConnector ivassDataConnector,
                          SearchServiceConnector searchServiceConnector) {
        log.trace("Initializing {}", OpenDataLoader.class.getSimpleName());
        this.openDataConnectors = openDataConnectors;
        this.institutionIndexWriterService = institutionIndexWriterService;
        this.categoryIndexWriterService = categoryIndexWriterService;
        this.aooIndexWriterService = aooIndexWriterService;
        this.uoIndexWriterService = uoIndexWriterService;
        this.stationIndexWriterService = stationIndexWriterService;
        this.ivassIndexWriterService = ivassIndexWriterService;
        this.anacService = anacService;
        this.ivassDataConnector = ivassDataConnector;
        this.searchServiceConnector = searchServiceConnector;
    }

    @Override
    public void run(String... args) {
        log.trace("run start");
        openDataConnectors.forEach(openDataConnector -> {
            institutionIndexWriterService.adds(openDataConnector.getInstitutions());
            categoryIndexWriterService.adds(openDataConnector.getCategories());
            aooIndexWriterService.adds(openDataConnector.getAOOs());
            uoIndexWriterService.adds(openDataConnector.getUOs());
            stationIndexWriterService.adds(anacService.loadStations());
            ivassIndexWriterService.adds(ivassDataConnector.getInsurances());
            //AI Azure search
            rebuildIndexIPA(openDataConnector);
        });

        log.trace("run end");
    }

    private void rebuildIndexIPA(OpenDataConnector openDataConnector) {
        List<Institution> institutions = openDataConnector.getInstitutions();
        if (!institutions.isEmpty()) {
            try {
                searchServiceConnector.deleteIndex(IPA_INDEX_NAME, INDEX_API_VERSION);
            } catch (FeignException.NotFound ignored) {}
            SearchIndexDefinition indexDefinition = buildIpaIndexDefinition();
            searchServiceConnector.createIndex(IPA_INDEX_NAME, INDEX_API_VERSION, indexDefinition);
            searchServiceConnector.indexInstitutionsIPA(institutions);
        }
    }

    private SearchIndexDefinition buildIpaIndexDefinition() {
        SearchIndexDefinition indexDefinition = new SearchIndexDefinition();
        indexDefinition.setName(IPA_INDEX_NAME);

        indexDefinition.setFields(List.of(
                AzureSearchField.field("id", "Edm.String", true, false, true, true),
                AzureSearchField.field("name", "Edm.String", false, true, false, false),
                AzureSearchField.field("city", "Edm.String", false, true, true, false)
        ));

        return indexDefinition;
    }
}
