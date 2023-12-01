package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution.Field;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import java.util.List;
import java.util.function.Function;

@Slf4j
abstract class IndexWriterServiceTemplate<T> implements IndexWriterService<T> {

    private final Function<T, Document> documentConverter;
    private final IndexWriterFactory indexWriterFactory;


    public IndexWriterServiceTemplate(IndexWriterFactory indexWriterFactory, Function<T, Document> documentConverter) {
        log.trace("Initializing {}", getClass().getSimpleName());
        this.indexWriterFactory = indexWriterFactory;
        this.documentConverter = documentConverter;
    }


    @SneakyThrows
    @Override
    public void adds(List<? extends T> items) {
        log.trace("adds start");
        log.debug("adds items = {}", items);
        final IndexWriter indexWriter = indexWriterFactory.create();
        try (indexWriter) {
            for (T item : items) {
                final Document doc = documentConverter.apply(item);
                indexWriter.updateDocument(new Term(Field.ID.toString(), getId(item)), doc);
            }
            indexWriter.commit();
        }
        log.trace("adds end");
    }


    @SneakyThrows
    @Override
    public void deleteAll() {
        log.trace("deleteAll start");
        final IndexWriter indexWriter = indexWriterFactory.create();
        try (indexWriter) {
            indexWriter.deleteAll();
            indexWriter.commit();
        }
        log.trace("deleteAll end");
    }
    protected abstract String getId(T item);

}
