package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Paths;
import java.util.List;

@Slf4j
//@Service
public class InstitutionFSIndexWriterService implements IndexWriterService<Institution> {

    private final InstitutionTokenAnalyzer institutionTokenAnalyzer;
    private final IndexWriter indexWriter;


    @SneakyThrows  //TODO
    @Autowired
    public InstitutionFSIndexWriterService(InstitutionTokenAnalyzer institutionTokenAnalyzer) {
        this.institutionTokenAnalyzer = institutionTokenAnalyzer;
        Directory directory = FSDirectory.open(Paths.get("index/institutions"));//FIXME: put into config variable
        final IndexWriterConfig indexConfig = new IndexWriterConfig(institutionTokenAnalyzer);
        indexWriter = new IndexWriter(directory, indexConfig);
    }

    @SneakyThrows //TODO
    @Override
    public void adds(List<? extends Institution> items) {
        try (indexWriter) {
            for (Institution item : items) {
                final Document doc = new Document();
                doc.add(new StringField("id", item.getId(), Field.Store.YES));
                doc.add(new StringField("originId", item.getOriginId(), Field.Store.YES));
                doc.add(new SortedDocValuesField("description", new BytesRef(item.getDescription())));
                doc.add(new TextField("description", item.getDescription(), Field.Store.YES));
                doc.add(new TextField("taxCode", item.getTaxCode(), Field.Store.YES));
                doc.add(new TextField("category", item.getCategory(), Field.Store.YES));
                doc.add(new TextField("digitalAddress", item.getDigitalAddress(), Field.Store.YES));
                doc.add(new TextField("address", item.getAddress(), Field.Store.YES));
                doc.add(new TextField("zipCode", item.getZipCode(), Field.Store.YES));
                doc.add(new TextField("origin", item.getOrigin(), Field.Store.YES));

                if (item.getO() != null) {
                    doc.add(new StoredField("o", item.getO()));
                }
                if (item.getOu() != null) {
                    doc.add(new StoredField("uo", item.getOu()));
                }
                if (item.getAoo() != null) {
                    doc.add(new StoredField("aoo", item.getAoo()));
                }

                indexWriter.updateDocument(new Term("id", item.getId()), doc);

            }
            indexWriter.commit();
        }
    }


    @SneakyThrows //TODO
    @Override
    public void deleteAll() {
        try (indexWriter) {
            indexWriter.deleteAll();
            indexWriter.commit();
        }
    }

}
