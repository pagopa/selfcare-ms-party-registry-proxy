package it.pagopa.selfcare.party.registry_proxy.connector.lucene.config;

import lombok.SneakyThrows;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.nio.file.Path;

@Configuration
@PropertySource("classpath:config/lucene-config.properties")
class FileSystemIndexConfig {

    @SneakyThrows
    @Bean
    public Directory institutionsDirectory(@Value("${lucene.index.institutions.folder}") String indexFolder) {
        return FSDirectory.open(Path.of(indexFolder));
    }


    @SneakyThrows
    @Bean
    public Directory categoriesDirectory(@Value("${lucene.index.categories.folder}") String indexFolder) {
        return FSDirectory.open(Path.of(indexFolder));
    }

}
