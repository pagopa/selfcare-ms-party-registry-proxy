package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class CategoryIndexWriterFactory extends IndexWriterFactoryTemplate {

    @Autowired
    public CategoryIndexWriterFactory(Directory categoriesDirectory, Analyzer categoryTokenAnalyzer) {
        super(categoriesDirectory, categoryTokenAnalyzer);
    }

}
