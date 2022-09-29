package it.pagopa.selfcare.party.registry_proxy.connector.lucene.config;

import lombok.SneakyThrows;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Configuration//TODO
@PropertySource("classpath:config/lucene-config.properties")
public class FileSystemIndexConfig {

//    private final InstitutionTokenAnalyzer institutionTokenAnalyzer;
//    private final Directory directory;
//
//
//    @SneakyThrows
//    @Autowired
//    public FileSystemIndexConfig(InstitutionTokenAnalyzer institutionTokenAnalyzer) {
//        this.institutionTokenAnalyzer = institutionTokenAnalyzer;
////        directory = FSDirectory.open(Paths.get("index/institutions"));//FIXME: put into config variable
//        final Path tempDirectory = Files.createTempDirectory("tmp_lucene_");
//        directory = FSDirectory.open(tempDirectory);
//        tempDirectory.toFile().deleteOnExit();//TODO: remove me
//    }


    @SneakyThrows
    @Bean
    public Directory institutionsDirectory(@Value("${lucene.index.institutions.folder}") Optional<Path> indexFolder) {
        return getDirectory(indexFolder, "lucene_institutions_");
    }


    @SneakyThrows
    @Bean
    public Directory categoriesDirectory(@Value("${lucene.index.categories.folder}") Optional<Path> indexFolder) {
        return getDirectory(indexFolder, "lucene_categories_");
    }


    private Directory getDirectory(@Value("${lucene.index.categories.folder}") Optional<Path> indexFolder, String tmpDirPrefix) throws IOException {
        Directory directory = FSDirectory.open(indexFolder.orElseGet(() -> {
            final Path tempDirectory;
            try {
                tempDirectory = Files.createTempDirectory(tmpDirPrefix);
                tempDirectory.toFile().deleteOnExit();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return tempDirectory;
        }));
        return directory;
    }

}
