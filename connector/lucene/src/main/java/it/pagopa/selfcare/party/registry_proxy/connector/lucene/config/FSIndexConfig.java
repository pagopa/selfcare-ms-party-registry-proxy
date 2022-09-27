package it.pagopa.selfcare.party.registry_proxy.connector.lucene.config;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.InstitutionTokenAnalyzer;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.reader.InstitutionFSIndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer.InstitutionFSIndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import lombok.SneakyThrows;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import java.nio.file.Files;
import java.nio.file.Path;

//@Configuration
public class FSIndexConfig {

    private final InstitutionTokenAnalyzer institutionTokenAnalyzer;
    private final Directory directory;


    @SneakyThrows
    @Autowired
    public FSIndexConfig(InstitutionTokenAnalyzer institutionTokenAnalyzer) {
        this.institutionTokenAnalyzer = institutionTokenAnalyzer;
//        directory = FSDirectory.open(Paths.get("index/institutions"));//FIXME: put into config variable
        final Path tempDirectory = Files.createTempDirectory("tmp_lucene_");
        directory = FSDirectory.open(tempDirectory);
        tempDirectory.toFile().deleteOnExit();//TODO: remove me
    }


    @Bean
    public IndexWriterService<Institution> institutionFSIndexWriterService() {
        return new InstitutionFSIndexWriterService(institutionTokenAnalyzer, directory);
    }


    @Bean
    @DependsOn("institutionFSIndexWriterService")
    public IndexSearchService<Institution> institutionFSIndexSearchService() {
        return new InstitutionFSIndexSearchService(institutionTokenAnalyzer, directory);
    }

}
