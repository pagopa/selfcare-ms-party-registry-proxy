package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.api.OpenDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InstitutionOpenDataLoader implements CommandLineRunner {

    private final OpenDataConnector openDataConnector;
    private final IndexWriterService<Institution> institutionIndexWriterService;
    private final IndexWriterService<Category> categoryIndexWriterService;


    @Autowired
    public InstitutionOpenDataLoader(OpenDataConnector openDataConnector,
                                     IndexWriterService<Institution> institutionIndexWriterService,
                                     IndexWriterService<Category> categoryIndexWriterService) {
        this.openDataConnector = openDataConnector;
        this.institutionIndexWriterService = institutionIndexWriterService;
        this.categoryIndexWriterService = categoryIndexWriterService;
    }


    @Override
    public void run(String... args) {
        institutionIndexWriterService.adds(openDataConnector.getInstitutions());
        categoryIndexWriterService.adds(openDataConnector.getCategories());
    }

}
