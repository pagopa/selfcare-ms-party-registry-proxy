package it.pagopa.selfcare.party.registry_proxy.connector.lucene.reader;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.InstitutionTokenAnalyzer;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.model.InstitutionFullTextQueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.model.converter.DocumentConverter;
import it.pagopa.selfcare.party.registry_proxy.connector.model.FullTextQueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution.Field;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
class InstitutionIndexSearchService implements IndexSearchService<Institution> {

    private final InstitutionTokenAnalyzer institutionTokenAnalyzer;
    private final Directory directory;
    private final DocumentConverter<Institution> documentConverter;
    private DirectoryReader reader;


    @SneakyThrows
    @Autowired
    public InstitutionIndexSearchService(InstitutionTokenAnalyzer institutionTokenAnalyzer,
                                         Directory directory,
                                         DocumentConverter<Institution> documentConverter) {
        this.institutionTokenAnalyzer = institutionTokenAnalyzer;
        this.directory = directory;
        this.documentConverter = documentConverter;
        if (DirectoryReader.indexExists(directory)) {
            reader = DirectoryReader.open(directory);
        }
    }


    @SneakyThrows
    private DirectoryReader refreshReader() {//FIXME
        if (reader == null) {
            reader = DirectoryReader.open(directory);
        } else {
            reader = Optional.ofNullable(DirectoryReader.openIfChanged(this.reader)).orElse(this.reader);
        }
        return reader;
    }


    @SneakyThrows
    @Override
    public FullTextQueryResult<Institution> fullTextSearch(Field searchingField, String searchText, int page, int limit) {
        DirectoryReader reader = refreshReader();
        final IndexSearcher indexSearcher = new IndexSearcher(reader);
        final TopScoreDocCollector collector = TopScoreDocCollector.create(reader.numDocs(), Integer.MAX_VALUE);
        final QueryParser parser = new QueryParser(searchingField.toString(), institutionTokenAnalyzer);
        parser.setPhraseSlop(4);
        indexSearcher.search(parser.parse(searchText), collector);
        final TopDocs hits = collector.topDocs((page - 1) * limit, limit);

        final List<Institution> institutions = Arrays.stream(hits.scoreDocs)
                .map(scoreDoc -> {
                    try {
                        return documentConverter.to(indexSearcher.doc(scoreDoc.doc));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

        final InstitutionFullTextQueryResult result = new InstitutionFullTextQueryResult();
        result.setItems(institutions);
        result.setTotalHits(hits.totalHits.value);

        return result;
    }


    @SneakyThrows
    public List<Institution> search(Field searchingField, String searchingValue) {
        DirectoryReader reader = refreshReader();
        final IndexSearcher indexSearcher = new IndexSearcher(reader);
        final TermQuery query = new TermQuery(new Term(searchingField.toString(), searchingValue));
        final TopDocs hits = indexSearcher.search(query, 1);

        return Arrays.stream(hits.scoreDocs)
                .map(scoreDoc -> {
                    try {
                        return documentConverter.to(indexSearcher.doc(scoreDoc.doc));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

}
