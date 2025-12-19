package it.pagopa.selfcare.party.registry_proxy.core;

import static it.pagopa.selfcare.party.registry_proxy.connector.model.AzureSearchField.field;
import static it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.Const.*;

import it.pagopa.selfcare.party.registry_proxy.connector.api.*;
import it.pagopa.selfcare.party.registry_proxy.connector.model.*;
import java.util.List;
import java.util.Objects;

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

    private final SearchIvassServiceConnector searchIvassServiceConnector;

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
                          SearchServiceConnector searchServiceConnector,
                          SearchIvassServiceConnector searchIvassServiceConnector) {
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
        this.searchIvassServiceConnector = searchIvassServiceConnector;
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
            rebuildIndexIVASS(ivassDataConnector);
            rebuildIndexIPA(openDataConnector);
        });

        log.trace("run end");
    }

    private void rebuildIndexIVASS(IvassDataConnector ivassDataConnector) {
        List<InsuranceCompany> companies = ivassDataConnector.getInsurances();

        List<InsuranceCompany> filteredCompanies = companies
                .stream()
                .filter(company -> Objects.nonNull(company.getId()) && !Objects.equals(company.getId(), ""))
                .toList();

        if (!filteredCompanies.isEmpty()) {
            try {
                searchServiceConnector.deleteIndex(IVASS_INDEX_NAME, INDEX_API_VERSION);
            } catch (Exception ignored) {}
//            SearchIndexDefinition indexDefinition = buildIvassIndexDefinition();
//            searchServiceConnector.createIndex(IVASS_INDEX_NAME, INDEX_API_VERSION, indexDefinition);
            searchIvassServiceConnector.indexIVASS(filteredCompanies);
        }
    }

    private void rebuildIndexIPA(OpenDataConnector openDataConnector) {
        List<Institution> institutions = openDataConnector.getInstitutions();

        List<Institution> filteredInstitutions = institutions
                .stream()
                .filter(institution -> Objects.nonNull(institution.getId()) && !Objects.equals(institution.getId(), ""))
                .toList();

        if (!filteredInstitutions.isEmpty()) {
            try {
                searchServiceConnector.deleteIndex(IPA_INDEX_NAME, INDEX_API_VERSION);
            } catch (Exception ignored) {}
//            SearchIndexDefinition indexDefinition = buildIpaIndexDefinition();
//            searchServiceConnector.createIndex(IPA_INDEX_NAME, INDEX_API_VERSION, indexDefinition);
            searchServiceConnector.indexInstitutionsIPA(filteredInstitutions);
        }
    }

    private SearchIndexDefinition buildIpaIndexDefinition() {

        SearchIndexDefinition indexDefinition = new SearchIndexDefinition();
        indexDefinition.setName(IPA_INDEX_NAME);

        indexDefinition.setFields(List.of(
                field("id", "Edm.String", true,  false, true,  true),

                field("entityType", "Edm.String", false, false, true,  false),
                field("originId", "Edm.String", false, false, true,  false),

                field("description", "Edm.String", false, true,  false, true),
                field("descriptionFull", "Edm.String", false, true,  false, true),

                field("taxCode", "Edm.String", false, false, true,  false),
                field("category", "Edm.String", false, false, true,  false),

                field("digitalAddress", "Edm.String", false, false, false, false),
                field("address", "Edm.String", false, true,  false, false),
                field("zipCode", "Edm.String", false, false, true,  false),

                field("origin", "Edm.String", false, false, true,  false),
                field("istatCode", "Edm.String", false, false, true,  false),

                field("o", "Edm.String", false, false, true,  false),
                field("ou", "Edm.String", false, false, true,  false),
                field("aoo", "Edm.String", false, false, true,  false)
        ));

        return indexDefinition;
    }

    private SearchIndexDefinition buildIvassIndexDefinition() {
        SearchIndexDefinition indexDefinition = new SearchIndexDefinition();
        indexDefinition.setName(IVASS_INDEX_NAME);

        indexDefinition.setFields(List.of(
                field("id", "Edm.String", true,  false, true,  true),

                field("originId", "Edm.String", false, false, true,  true),

                field("description", "Edm.String", false, true,  false, true),

                field("taxCode", "Edm.String", false, false, false,  false),

                field("workType", "Edm.String", false, false, false,  false),

                field("registerType", "Edm.String", false, false, false,  false),

                field("address", "Edm.String", false, false,  false, false),

                field("digitalAddress", "Edm.String", false, false, false, false),

                field("origin", "Edm.String", false, false, false,  false)
                ));

        return indexDefinition;
    }



}
