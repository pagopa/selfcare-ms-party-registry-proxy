package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("pdndIndexWriterFactory")
class PDNDIndexWriterFactory extends IndexWriterFactoryTemplate {

    @Autowired
    public PDNDIndexWriterFactory(Directory pdndDirectory, @Qualifier("pdndTokenAnalyzer") Analyzer pdndTokenAnalyzer) {
        super(pdndDirectory, pdndTokenAnalyzer);
    }

}
