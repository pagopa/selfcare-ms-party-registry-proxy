package it.pagopa.selfcare.party.registry_proxy.connector.lucene.reader;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.InstitutionTokenAnalyzer;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter.DocumentToInstitutionConverter;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.model.InstitutionQueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryFilter;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchField;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
class InstitutionIndexSearchService implements IndexSearchService<Institution> {

    private final Analyzer institutionTokenAnalyzer = new InstitutionTokenAnalyzer();
    private final Function<Document, Institution> documentConverter = new DocumentToInstitutionConverter();
    private final DirectoryReaderFactory directoryReaderFactory;


    @SneakyThrows
    @Autowired
    public InstitutionIndexSearchService(Directory institutionsDirectory) {
        directoryReaderFactory = new DirectoryReaderFactory(institutionsDirectory);
    }


    @SneakyThrows
    @Override
    public QueryResult<Institution> fullTextSearch(SearchField field, String value, int page, int limit) {
        final DirectoryReader reader = directoryReaderFactory.create();
        final IndexSearcher indexSearcher = new IndexSearcher(reader);
        final TopScoreDocCollector collector = TopScoreDocCollector.create(reader.numDocs(), Integer.MAX_VALUE);
        final QueryParser parser = new QueryParser(field.toString(), institutionTokenAnalyzer);
        parser.setPhraseSlop(4);
        indexSearcher.search(parser.parse(value), collector);
        final TopDocs hits = collector.topDocs((page - 1) * limit, limit);

        final List<Institution> institutions = new ArrayList<>(hits.scoreDocs.length);
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            institutions.add(documentConverter.apply(indexSearcher.doc(scoreDoc.doc)));
        }

        final InstitutionQueryResult result = new InstitutionQueryResult();
        result.setItems(institutions);
        result.setTotalHits(hits.totalHits.value);

        return result;
    }


    @SneakyThrows
    @Override
    public List<Institution> findById(SearchField field, String value) {
        final DirectoryReader reader = directoryReaderFactory.create();
        final IndexSearcher indexSearcher = new IndexSearcher(reader);
        final TermQuery query = new TermQuery(new Term(field.toString(), value));
        final TopDocs hits = indexSearcher.search(query, 1);

        final List<Institution> institutions = new ArrayList<>(hits.scoreDocs.length);
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            institutions.add(documentConverter.apply(indexSearcher.doc(scoreDoc.doc)));
        }

        return institutions;
    }


    @SneakyThrows
    @Override
    public QueryResult<Institution> findAll(int page, int limit, QueryFilter... filters) {
        final DirectoryReader reader = directoryReaderFactory.create();
        final IndexSearcher indexSearcher = new IndexSearcher(reader);
        final Query query;
        if (filters == null || filters.length == 0) {
            query = new MatchAllDocsQuery();
        } else {
            final BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            for (QueryFilter filter : filters) {
                final TermQuery termQuery = new TermQuery(new Term(filter.getField().toString(), filter.getValue()));
                queryBuilder.add(termQuery, BooleanClause.Occur.MUST);
            }
            query = queryBuilder.build();
        }
        final TopScoreDocCollector collector = TopScoreDocCollector.create(reader.numDocs(), reader.numDocs());
        indexSearcher.search(query, collector);

        final TopDocs hits = collector.topDocs((page - 1) * limit, limit);

        final List<Institution> institutions = new ArrayList<>(hits.scoreDocs.length);
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            institutions.add(documentConverter.apply(indexSearcher.doc(scoreDoc.doc)));
        }

        final InstitutionQueryResult result = new InstitutionQueryResult();
        result.setItems(institutions);
        result.setTotalHits(hits.totalHits.value);

        return result;
    }

}
