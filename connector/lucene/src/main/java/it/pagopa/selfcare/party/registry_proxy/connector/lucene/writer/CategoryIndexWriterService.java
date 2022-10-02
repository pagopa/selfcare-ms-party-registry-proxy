package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter.CategoryToDocumentConverter;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution.Field;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
class CategoryIndexWriterService implements IndexWriterService<Category> {

    private final Function<Category, Document> documentConverter = new CategoryToDocumentConverter();
    private final IndexWriterFactory indexWriterFactory;


    @SneakyThrows
    @Autowired
    public CategoryIndexWriterService(IndexWriterFactory categoryIndexWriterFactory) {
        this.indexWriterFactory = categoryIndexWriterFactory;
    }


    @SneakyThrows
    @Override
    public void adds(List<? extends Category> items) {
        final IndexWriter indexWriter = indexWriterFactory.create();
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
        final IndexWriter indexWriter = indexWriterFactory.create();
        try (indexWriter) {
            indexWriter.deleteAll();
            indexWriter.commit();
        }
    }

}
