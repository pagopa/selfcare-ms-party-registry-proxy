package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.InstitutionTokenAnalyzer;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.config.InMemoryIndexConfig;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        InMemoryIndexConfig.class,
        InstitutionTokenAnalyzer.class,
        InstitutionIndexWriterFactory.class
})
class InstitutionIndexWriterFactoryTest {

    @Autowired
    private Directory institutionsDirectory;

    @Autowired
    private InstitutionIndexWriterFactory indexWriterFactory;


    @Test
    void create() {
        // given
        // when
        final IndexWriter indexWriter = indexWriterFactory.create();
        // then
        Assertions.assertNotNull(indexWriter);
        Assertions.assertTrue(indexWriter.isOpen());
        Assertions.assertSame(institutionsDirectory, indexWriter.getDirectory());
        Assertions.assertEquals(InstitutionTokenAnalyzer.class, indexWriter.getAnalyzer().getClass());
    }

}