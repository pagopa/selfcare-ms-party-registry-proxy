package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.analysis.AOOTokenAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class AOOIndexWriterFactory extends IndexWriterFactoryTemplate {

    @Autowired //TO-DO generelizzare il token analyzer
    public AOOIndexWriterFactory(Directory aoosDirectory, AOOTokenAnalyzer aooTokenAnalyzer) {
        super(aoosDirectory, aooTokenAnalyzer);
    }

}
