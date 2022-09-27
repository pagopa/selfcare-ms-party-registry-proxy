package it.pagopa.selfcare.party.registry_proxy.connector.lucene.config;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.InstitutionTokenAnalyzer;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.reader.InstitutionRAMIndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer.InstitutionRAMIndexWriterService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class InMemoryIndexConfig {

    private final InstitutionTokenAnalyzer institutionTokenAnalyzer;
    private final Directory directory;


    @Autowired
    public InMemoryIndexConfig(InstitutionTokenAnalyzer institutionTokenAnalyzer) {
        this.institutionTokenAnalyzer = institutionTokenAnalyzer;
        directory = new RAMDirectory();
    }


    @Bean
    public IndexWriterService<Institution> institutionRAMIndexWriterService() {
        return new InstitutionRAMIndexWriterService(institutionTokenAnalyzer, directory);
    }


    @Bean
    @DependsOn("institutionRAMIndexWriterService")
    public IndexSearchService<Institution> institutionRAMIndexSearchService() {
        return new InstitutionRAMIndexSearchService(institutionTokenAnalyzer, directory);
    }

}
