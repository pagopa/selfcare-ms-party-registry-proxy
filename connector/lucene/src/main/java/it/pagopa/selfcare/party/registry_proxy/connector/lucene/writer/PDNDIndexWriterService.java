package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter.PDNDToDocumentConverter;
import it.pagopa.selfcare.party.registry_proxy.connector.model.PDND;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class PDNDIndexWriterService extends IndexWriterServiceTemplate<PDND> {

    @Autowired
    public PDNDIndexWriterService(@Qualifier("pdndIndexWriterFactory") IndexWriterFactory pdndIndexWriterFactory) {
        super(pdndIndexWriterFactory, new PDNDToDocumentConverter());
    }

    @Override
    protected String getId(PDND item) {
        return item.getId();
    }

}
