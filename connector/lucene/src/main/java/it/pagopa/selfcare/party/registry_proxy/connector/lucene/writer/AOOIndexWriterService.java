package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter.AOOToDocumentConverter;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class AOOIndexWriterService extends IndexWriterServiceTemplate<AOO> {


    @Autowired //TO-DO generalizzare l'indexWriterFactory
    public AOOIndexWriterService(AOOIndexWriterFactory aooIndexWriterFactory) {
        super(aooIndexWriterFactory, new AOOToDocumentConverter());
    }

    @Override
    protected String getId(AOO item) {
        return item.getId();
    }

}
