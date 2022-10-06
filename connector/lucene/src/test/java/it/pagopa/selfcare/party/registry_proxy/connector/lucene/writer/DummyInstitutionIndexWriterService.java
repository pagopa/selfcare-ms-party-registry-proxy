package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DummyInstitutionIndexWriterService implements IndexWriterService<Institution> {

    private final IndexWriterService<Institution> indexWriterService;


    @Autowired
    public DummyInstitutionIndexWriterService(IndexWriterFactory institutionIndexWriterFactory) {
        indexWriterService = new InstitutionIndexWriterService(institutionIndexWriterFactory);
    }


    @Override
    public void adds(List<? extends Institution> items) {
        indexWriterService.adds(items);
    }


    @Override
    public void deleteAll() {
        indexWriterService.deleteAll();
    }

}
