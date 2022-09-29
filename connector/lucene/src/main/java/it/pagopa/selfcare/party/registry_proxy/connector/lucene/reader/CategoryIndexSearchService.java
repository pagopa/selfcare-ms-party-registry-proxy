package it.pagopa.selfcare.party.registry_proxy.connector.lucene.reader;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.CategoryTokenAnalyzer;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.model.CategoryQueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryFilter;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchField;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
class CategoryIndexSearchService implements IndexSearchService<Category> {

    private final CategoryTokenAnalyzer categoryTokenAnalyzer;
    private final Directory directory;
    private final Function<Document, Category> documentConverter;
    private DirectoryReader reader;


    @SneakyThrows
    @Autowired
    public CategoryIndexSearchService(CategoryTokenAnalyzer categoryTokenAnalyzer,
                                      Directory categoriesDirectory,
                                      Function<Document, Category> documentConverter) {
        this.categoryTokenAnalyzer = categoryTokenAnalyzer;
        this.directory = categoriesDirectory;
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
    public QueryResult<Category> fullTextSearch(SearchField field, String value, int page, int limit) {
        DirectoryReader reader = refreshReader();
        final IndexSearcher indexSearcher = new IndexSearcher(reader);
        final TopScoreDocCollector collector = TopScoreDocCollector.create(reader.numDocs(), Integer.MAX_VALUE);
        final QueryParser parser = new QueryParser(field.toString(), categoryTokenAnalyzer);
        parser.setPhraseSlop(4);
        indexSearcher.search(parser.parse(value), collector);
        final TopDocs hits = collector.topDocs((page - 1) * limit, limit);

        final List<Category> categories = Arrays.stream(hits.scoreDocs)
                .map(scoreDoc -> {
                    try {
                        return documentConverter.apply(indexSearcher.doc(scoreDoc.doc));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

        final CategoryQueryResult result = new CategoryQueryResult();
        result.setItems(categories);
        result.setTotalHits(hits.totalHits.value);

        return result;
    }


    @SneakyThrows
    @Override
    public List<Category> findById(SearchField field, String value) {
        DirectoryReader reader = refreshReader();
        final IndexSearcher indexSearcher = new IndexSearcher(reader);
        final TermQuery query = new TermQuery(new Term(field.toString(), value));
        final TopDocs hits = indexSearcher.search(query, 1);

        return Arrays.stream(hits.scoreDocs)
                .map(scoreDoc -> {
                    try {
                        return documentConverter.apply(indexSearcher.doc(scoreDoc.doc));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }


    @SneakyThrows
    @Override
    public QueryResult<Category> findAll(int page, int limit, QueryFilter... filters) {
        DirectoryReader reader = refreshReader();
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

        final List<Category> categories = Arrays.stream(hits.scoreDocs)
                .map(scoreDoc -> {
                    try {
                        return documentConverter.apply(indexSearcher.doc(scoreDoc.doc));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

        final CategoryQueryResult result = new CategoryQueryResult();
        result.setItems(categories);
        result.setTotalHits(hits.totalHits.value);

        return result;
    }

}
