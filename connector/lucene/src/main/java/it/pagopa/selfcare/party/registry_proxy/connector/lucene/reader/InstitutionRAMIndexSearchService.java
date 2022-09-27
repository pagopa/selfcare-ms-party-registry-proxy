package it.pagopa.selfcare.party.registry_proxy.connector.lucene.reader;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.InstitutionDocumentConverter;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.InstitutionTokenAnalyzer;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class InstitutionRAMIndexSearchService implements IndexSearchService<Institution> {

    private final InstitutionTokenAnalyzer institutionTokenAnalyzer;
    private DirectoryReader reader;
    private final Directory directory;


    @SneakyThrows  //TODO
    @Autowired
    public InstitutionRAMIndexSearchService(InstitutionTokenAnalyzer institutionTokenAnalyzer, Directory directory) {
        this.institutionTokenAnalyzer = institutionTokenAnalyzer;
        this.directory = directory;
        if (DirectoryReader.indexExists(directory)) {
            reader = DirectoryReader.open(directory);
        }
    }


    @SneakyThrows //TODO
    @Override
    public List<Institution> searchByText(String searchingField, String searchText, int page, int limit) {
        if (DirectoryReader.indexExists(directory)) {
            this.reader = DirectoryReader.open(directory);//TODO
        }
        final DirectoryReader reader = Optional.ofNullable(DirectoryReader.openIfChanged(this.reader)).orElse(this.reader);
        final IndexSearcher indexSearcher = new IndexSearcher(reader);

        final TopScoreDocCollector collector = TopScoreDocCollector.create(reader.numDocs(), Integer.MAX_VALUE);
        final QueryParser parser = new QueryParser(searchingField, institutionTokenAnalyzer);
        parser.setPhraseSlop(4);
        indexSearcher.search(parser.parse(searchText), collector);
        final TopDocs hits = collector.topDocs((page - 1) * limit, limit);

        final List<Institution> institutions = Arrays.stream(hits.scoreDocs)
                .map(scoreDoc -> {
                    try {
                        return new InstitutionDocumentConverter().to(indexSearcher.doc(scoreDoc.doc));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

//        hits.totalHits.value; //TODO

        return institutions;
    }

}
