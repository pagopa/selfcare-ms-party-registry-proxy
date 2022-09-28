package it.pagopa.selfcare.party.registry_proxy.connector.lucene.config;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.InstitutionTokenAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration//TODO
public class InMemoryIndexConfig {

    private final InstitutionTokenAnalyzer institutionTokenAnalyzer;
    private final Directory directory;


    @Autowired
    public InMemoryIndexConfig(InstitutionTokenAnalyzer institutionTokenAnalyzer) {
        this.institutionTokenAnalyzer = institutionTokenAnalyzer;
        directory = new RAMDirectory();
    }


    @Bean
    public Directory directory() {
        return directory;
    }


//    @Bean
//    public IndexWriterService<Institution> institutionRAMIndexWriterService(DocumentConverter<Institution> documentConverter) {
//        return new InstitutionIndexWriterService(institutionTokenAnalyzer, directory, documentConverter);
//    }
//
//
//    @Bean
//    @DependsOn("institutionRAMIndexWriterService")
//    public IndexSearchService<Institution> institutionRAMIndexSearchService(DocumentConverter<Institution> documentConverter) {
//        return new InstitutionIndexSearchService(institutionTokenAnalyzer, directory, documentConverter);
//    }

}
