package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.api.OpenDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OpenDataLoader implements CommandLineRunner {

    private final List<OpenDataConnector> openDataConnectors;
    private final IndexWriterService<Institution> institutionIndexWriterService;
    private final IndexWriterService<Category> categoryIndexWriterService;


    @Autowired
    public OpenDataLoader(List<OpenDataConnector> openDataConnectors,
                          IndexWriterService<Institution> institutionIndexWriterService,
                          BeanFactory beanFactory,
                          IndexWriterService<Category> categoryIndexWriterService) {
        this.openDataConnectors = openDataConnectors;
        this.institutionIndexWriterService = institutionIndexWriterService;
        this.categoryIndexWriterService = categoryIndexWriterService;
    }


    @Override
    public void run(String... args) {
        openDataConnectors.forEach(openDataConnector -> {
            institutionIndexWriterService.adds(openDataConnector.getInstitutions());
            categoryIndexWriterService.adds(openDataConnector.getCategories());
        });
    }

}
