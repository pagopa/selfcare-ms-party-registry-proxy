package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter.InstitutionToDocumentConverter;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
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
class InstitutionIndexWriterService implements IndexWriterService<Institution> {

    private final Function<Institution, Document> documentConverter = new InstitutionToDocumentConverter();
    private final IndexWriterFactory indexWriterFactory;


    @SneakyThrows
    @Autowired
    public InstitutionIndexWriterService(IndexWriterFactory institutionIndexWriterFactory) {
        indexWriterFactory = institutionIndexWriterFactory;
    }


    @SneakyThrows
    @Override
    public void adds(List<? extends Institution> items) {
        final IndexWriter indexWriter = indexWriterFactory.create();
        try (indexWriter) {
            for (Institution item : items) {
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
