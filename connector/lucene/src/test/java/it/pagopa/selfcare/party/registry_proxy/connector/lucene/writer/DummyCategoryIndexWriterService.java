package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DummyCategoryIndexWriterService implements IndexWriterService<Category> {

    private final IndexWriterService<Category> indexWriterService;


    @Autowired
    public DummyCategoryIndexWriterService(IndexWriterFactory categoryIndexWriterFactory) {
        indexWriterService = new CategoryIndexWriterService(categoryIndexWriterFactory);
    }


    @Override
    public void adds(List<? extends Category> items) {
        indexWriterService.adds(items);
    }


    @Override
    public void deleteAll() {
        indexWriterService.deleteAll();
    }

}
