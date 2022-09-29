package it.pagopa.selfcare.party.registry_proxy.connector.lucene.config;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.context.annotation.Bean;

//@Configuration//TODO
public class InMemoryIndexConfig {

    @Bean
    public Directory ramDirectory() {
        return new RAMDirectory();
    }

}
