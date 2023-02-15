package it.pagopa.selfcare.party.registry_proxy.connector.lucene.reader;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
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
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
abstract class IndexSearchServiceTemplate<T> implements IndexSearchService<T> {

    private static final String FIELD_IS_REQUIRED = "A search field is required";
    private static final String VALUE_IS_REQUIRED = "A search value is required";

    private final DirectoryReaderFactory directoryReaderFactory;
    private final Analyzer analyzer;
    private final Function<Document, T> documentConverter;


    public IndexSearchServiceTemplate(Directory directory, Analyzer analyzer, Function<Document, T> documentConverter) {
        log.trace("Initializing {}", getClass().getSimpleName());
        directoryReaderFactory = new DirectoryReaderFactory(directory);
        this.analyzer = analyzer;
        this.documentConverter = documentConverter;
    }


    @SneakyThrows
    @Override
    public QueryResult<T> fullTextSearch(SearchField field, String value, int page, int limit) {
        log.trace("fullTextSearch start");
        log.debug("fullTextSearch field = {}, value = {}, page = {}, limit = {}", field, value, page, limit);
        Assert.notNull(field, FIELD_IS_REQUIRED);
        Assert.notNull(value, VALUE_IS_REQUIRED);
        Assert.isTrue(page > 0, "A page number must be great than 0");
        Assert.isTrue(limit > 0, "A limit result must be great than or equal to 1");
        final DirectoryReader reader = directoryReaderFactory.create();
        final IndexSearcher indexSearcher = new IndexSearcher(reader);
        final TopScoreDocCollector collector = TopScoreDocCollector.create(reader.numDocs(), Integer.MAX_VALUE);
        final QueryParser parser = new QueryParser(field.toString(), analyzer);
        parser.setPhraseSlop(4);
        indexSearcher.search(parser.parse(value), collector);
        final TopDocs hits = collector.topDocs((page - 1) * limit, limit);

        final List<T> categories = new ArrayList<>(hits.scoreDocs.length);
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            categories.add(documentConverter.apply(indexSearcher.doc(scoreDoc.doc)));
        }

        final QueryResult<T> queryResult = getQueryResult(categories, hits.totalHits.value);
        log.debug("fullTextSearch result = {}", queryResult);
        log.trace("fullTextSearch end");
        return queryResult;
    }

    @SneakyThrows
    @Override
    public QueryResult<T> fullTextSearch(SearchField field, String value, SearchField filter, String categories, int page, int limit) {
        log.trace("fullTextSearch start");
        log.debug("fullTextSearch field = {}, value = {}, filter = {}, categories = {}, page = {}, limit = {}", field, value, filter, categories, page, limit);
        Assert.notNull(field, FIELD_IS_REQUIRED);
        Assert.notNull(value, VALUE_IS_REQUIRED);
        Assert.notNull(filter, FIELD_IS_REQUIRED);
        Assert.notNull(categories, VALUE_IS_REQUIRED);
        Assert.isTrue(page > 0, "A page number must be great than 0");
        Assert.isTrue(limit > 0, "A limit result must be great than or equal to 1");
        final DirectoryReader reader = directoryReaderFactory.create();
        final IndexSearcher indexSearcher = new IndexSearcher(reader);
        final TopScoreDocCollector collector = TopScoreDocCollector.create(reader.numDocs(), Integer.MAX_VALUE);

       // FUNZIONA ABBASTANZA (Da rivedere)
        final QueryParser parser = new QueryParser(field.toString(), analyzer);
        parser.setPhraseSlop(4);
        indexSearcher.search(parser.parse(value + " AND (L6 OR L4 OR L45)"), collector);
        final TopDocs hits = collector.topDocs((page - 1) * limit, limit);

        final List<T> foundCategories = new ArrayList<>(hits.scoreDocs.length);
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            foundCategories.add(documentConverter.apply(indexSearcher.doc(scoreDoc.doc)));
        }

        final QueryResult<T> queryResult = getQueryResult(foundCategories, hits.totalHits.value);
        log.debug("fullTextSearch result = {}", queryResult);
        log.trace("fullTextSearch end");
        return queryResult;
    }


    @SneakyThrows
    @Override
    public List<T> findById(SearchField field, String value) {
        log.trace("findById start");
        log.debug("findById field = {}, value = {}", field, value);
        Assert.notNull(field, FIELD_IS_REQUIRED);
        Assert.notNull(value, VALUE_IS_REQUIRED);
        final DirectoryReader reader = directoryReaderFactory.create();
        final IndexSearcher indexSearcher = new IndexSearcher(reader);
        final TermQuery query = new TermQuery(new Term(field.toString(), value));
        final TopDocs hits = indexSearcher.search(query, 1);

        final List<T> items = new ArrayList<>(hits.scoreDocs.length);
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            items.add(documentConverter.apply(indexSearcher.doc(scoreDoc.doc)));
        }

        log.debug("findById result = {}", items);
        log.trace("findById end");
        return items;
    }


    @SneakyThrows
    @Override
    public QueryResult<T> findAll(int page, int limit, QueryFilter... filters) {
        log.trace("findAll start");
        log.debug("findAll page = {}, limit = {}, filters = {}", page, limit, filters);
        final DirectoryReader reader = directoryReaderFactory.create();
        final IndexSearcher indexSearcher = new IndexSearcher(reader);
        final Query query;
        if (filters == null || filters.length == 0) {
            query = new MatchAllDocsQuery();
        } else {
            final BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            for (QueryFilter filter : filters) {
                Assert.notNull(filter.getField(), FIELD_IS_REQUIRED);
                Assert.notNull(filter.getValue(), VALUE_IS_REQUIRED);
                final TermQuery termQuery = new TermQuery(new Term(filter.getField().toString(), filter.getValue()));
                queryBuilder.add(termQuery, BooleanClause.Occur.MUST);
            }
            query = queryBuilder.build();
        }
        final TopScoreDocCollector collector = TopScoreDocCollector.create(reader.numDocs(), reader.numDocs());
        indexSearcher.search(query, collector);

        final TopDocs hits = collector.topDocs((page - 1) * limit, limit);

        final List<T> items = new ArrayList<>(hits.scoreDocs.length);
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            items.add(documentConverter.apply(indexSearcher.doc(scoreDoc.doc)));
        }

        final QueryResult<T> queryResult = getQueryResult(items, hits.totalHits.value);
        log.debug("findAll result = {}", queryResult);
        log.trace("findAll end");
        return queryResult;
    }


    protected abstract QueryResult<T> getQueryResult(List<T> items, long totalHits);



}
