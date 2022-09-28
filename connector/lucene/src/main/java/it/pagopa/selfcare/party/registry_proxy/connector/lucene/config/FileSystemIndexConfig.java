package it.pagopa.selfcare.party.registry_proxy.connector.lucene.config;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.InstitutionTokenAnalyzer;
import lombok.SneakyThrows;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.nio.file.Files;
import java.nio.file.Path;

//@Configuration//TODO
public class FileSystemIndexConfig {

    private final InstitutionTokenAnalyzer institutionTokenAnalyzer;
    private final Directory directory;


    @SneakyThrows
    @Autowired
    public FileSystemIndexConfig(InstitutionTokenAnalyzer institutionTokenAnalyzer) {
        this.institutionTokenAnalyzer = institutionTokenAnalyzer;
//        directory = FSDirectory.open(Paths.get("index/institutions"));//FIXME: put into config variable
        final Path tempDirectory = Files.createTempDirectory("tmp_lucene_");
        directory = FSDirectory.open(tempDirectory);
        tempDirectory.toFile().deleteOnExit();//TODO: remove me
    }

    @Bean
    public Directory directory() {
        return directory;
    }


//    @Bean
//    public IndexWriterService<Institution> institutionFSIndexWriterService(DocumentConverter<Institution> documentConverter) {
//        return new InstitutionIndexWriterService(institutionTokenAnalyzer, directory, documentConverter);
//    }
//
//
//    @Bean
//    @DependsOn("institutionFSIndexWriterService")
//    public IndexSearchService<Institution> institutionFSIndexSearchService(DocumentConverter<Institution> documentConverter) {
//        return new InstitutionIndexSearchService(institutionTokenAnalyzer, directory, documentConverter);
//    }

}
