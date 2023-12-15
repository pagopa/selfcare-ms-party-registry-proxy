package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;

import java.util.List;

public class DummyUOIndexWriterService implements IndexWriterService<UO> {

    private final IndexWriterService<UO> indexWriterService;


    public DummyUOIndexWriterService(UOIndexWriterFactory indexWriterFactory) {
        indexWriterService = new UOIndexWriterService(indexWriterFactory);
    }


    @Override
    public void adds(List<? extends UO> items) {
        indexWriterService.adds(items);
    }


    @Override
    public void deleteAll() {
        indexWriterService.deleteAll();
    }

    @Override
    public void cleanIndex(String entityType) {
        indexWriterService.cleanIndex(entityType);
    }
}
