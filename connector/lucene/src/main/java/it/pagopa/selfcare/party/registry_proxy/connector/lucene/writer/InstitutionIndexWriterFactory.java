package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class InstitutionIndexWriterFactory extends IndexWriterFactoryTemplate {

    @Autowired
    public InstitutionIndexWriterFactory(Directory institutionsDirectory, Analyzer institutionTokenAnalyzer) {
        super(institutionsDirectory, institutionTokenAnalyzer);
    }

}
