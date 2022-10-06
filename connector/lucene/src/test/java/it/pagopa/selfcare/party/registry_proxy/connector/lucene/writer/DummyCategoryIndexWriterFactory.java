package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.CategoryTokenAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DummyCategoryIndexWriterFactory implements IndexWriterFactory {

    private final IndexWriterFactory indexWriterFactory;


    @Autowired
    public DummyCategoryIndexWriterFactory(Directory categoriesDirectory) {
        indexWriterFactory = new CategoryIndexWriterFactory(categoriesDirectory, new CategoryTokenAnalyzer());
    }


    @Override
    public IndexWriter create() {
        return indexWriterFactory.create();
    }

}
