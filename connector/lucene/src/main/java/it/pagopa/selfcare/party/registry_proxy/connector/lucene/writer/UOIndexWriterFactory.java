package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.UOTokenAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class UOIndexWriterFactory extends IndexWriterFactoryTemplate {

    @Autowired //TO-DO generelizzare il token analyzer
    public UOIndexWriterFactory(Directory uosDirectory, UOTokenAnalyzer uoTokenAnalyzer) {
        super(uosDirectory, uoTokenAnalyzer);
    }

}
