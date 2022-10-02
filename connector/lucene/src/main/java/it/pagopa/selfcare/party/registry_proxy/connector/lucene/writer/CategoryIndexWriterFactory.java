package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class CategoryIndexWriterFactory implements IndexWriterFactory {

    private final Directory directory;
    private final Analyzer analyzer;


    @Autowired
    public CategoryIndexWriterFactory(Directory categoriesDirectory, Analyzer categoryTokenAnalyzer) {
        this.directory = categoriesDirectory;
        this.analyzer = categoryTokenAnalyzer;
    }


    @SneakyThrows
    @Override
    public IndexWriter create() {
        return new IndexWriter(directory, new IndexWriterConfig(analyzer));
    }

}
