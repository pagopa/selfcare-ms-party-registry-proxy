package it.pagopa.selfcare.party.registry_proxy.connector;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.api.OpenDataConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InstitutionOpenDataLoader implements CommandLineRunner {

    private final OpenDataConnector openDataConnector;
    private final IndexWriterService<Institution> indexWriterService;


    @Autowired
    public InstitutionOpenDataLoader(OpenDataConnector openDataConnector, IndexWriterService indexWriterService) {
        this.openDataConnector = openDataConnector;
        this.indexWriterService = indexWriterService;
    }


    @Override
    public void run(String... args) throws Exception {
//        final List<? extends Institution> institutions = openDataConnector.getInstitutions();
//        indexWriterService.adds(institutions);
    }

}
