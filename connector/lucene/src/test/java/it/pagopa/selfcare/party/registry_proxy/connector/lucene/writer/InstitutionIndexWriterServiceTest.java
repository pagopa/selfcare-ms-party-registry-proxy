package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.InstitutionTokenAnalyzer;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.config.InMemoryIndexConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.model.DummyInstitution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import lombok.SneakyThrows;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.store.Directory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        InMemoryIndexConfig.class,
        InstitutionTokenAnalyzer.class,
        InstitutionIndexWriterFactory.class,
        InstitutionIndexWriterService.class
})
class InstitutionIndexWriterServiceTest {

    @Autowired
    @Qualifier("institutionsDirectory")
    private Directory directory;

    @Autowired
    private InstitutionIndexWriterService indexWriterService;


    @SneakyThrows
    private long count(Directory directory) {
        final DirectoryReader reader = DirectoryReader.open(this.directory);
        final IndexSearcher indexSearcher = new IndexSearcher(reader);
        final MatchAllDocsQuery query = new MatchAllDocsQuery();
        return indexSearcher.count(query);
    }


    @Test
    void adds() {
        // given
        final List<? extends Institution> items = List.of(new DummyInstitution());
        // when
        indexWriterService.adds(items);
        // then
        assertEquals(1, count(directory));
    }


    @Test
    void deleteAll() {
        // given
        // when
        indexWriterService.deleteAll();
        // then
        assertEquals(0, count(directory));
    }

}