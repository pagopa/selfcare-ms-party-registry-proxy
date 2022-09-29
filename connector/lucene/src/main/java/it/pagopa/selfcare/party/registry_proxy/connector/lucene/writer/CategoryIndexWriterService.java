package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.CategoryTokenAnalyzer;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution.Field;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
class CategoryIndexWriterService implements IndexWriterService<Category> {

    private final IndexWriter indexWriter;
    private final Function<Category, Document> documentConverter;


    @SneakyThrows
    @Autowired
    public CategoryIndexWriterService(CategoryTokenAnalyzer categoryTokenAnalyzer,
                                      Function<Category, Document> documentConverter,
                                      Directory categoriesDirectory) {
        this.documentConverter = documentConverter;
        final IndexWriterConfig indexConfig = new IndexWriterConfig(categoryTokenAnalyzer);
        indexWriter = new IndexWriter(categoriesDirectory, indexConfig);
    }


    @SneakyThrows
    @Override
    public void adds(List<? extends Category> items) {
        try (indexWriter) {
            for (Category item : items) {
                final Document doc = documentConverter.apply(item);
                indexWriter.updateDocument(new Term(Field.ID.toString(), item.getId()), doc);
            }
            indexWriter.commit();
        }
    }


    @SneakyThrows
    @Override
    public void deleteAll() {
        try (indexWriter) {
            indexWriter.deleteAll();
            indexWriter.commit();
        }
    }

}
