package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.StationTokenAnalyzer;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.config.InMemoryIndexConfig;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        InMemoryIndexConfig.class,
        StationTokenAnalyzer.class,
        PDNDIndexWriterFactory.class
})
class PDNDIndexWriterFactoryTest {

    @Autowired
    private Directory pdndDirectory;

    @Autowired
    private PDNDIndexWriterFactory indexWriterFactory;


    @Test
    void create() {
        // given
        // when
        final IndexWriter indexWriter = indexWriterFactory.create();
        // then
        assertNotNull(indexWriter);
        assertTrue(indexWriter.isOpen());
        assertSame(pdndDirectory, indexWriter.getDirectory());
        assertEquals(StationTokenAnalyzer.class, indexWriter.getAnalyzer().getClass());
    }


    @Test
    void create_Exception() {
        // given
        final IndexWriterFactory indexWriterFactory = new PDNDIndexWriterFactory(null, new StationTokenAnalyzer());
        // when
        final Executable executable = indexWriterFactory::create;
        // then
        assertThrows(RuntimeException.class, executable);
    }

}