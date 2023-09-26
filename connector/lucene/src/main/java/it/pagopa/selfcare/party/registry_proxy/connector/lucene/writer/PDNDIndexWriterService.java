package it.pagopa.selfcare.party.registry_proxy.connector.lucene.writer;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter.StationToDocumentConverter;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class PDNDIndexWriterService extends IndexWriterServiceTemplate<Station> {

    @Autowired
    public PDNDIndexWriterService(@Qualifier("pdndIndexWriterFactory") IndexWriterFactory pdndIndexWriterFactory) {
        super(pdndIndexWriterFactory, new StationToDocumentConverter());
    }

    @Override
    protected String getId(Station item) {
        return item.getId();
    }

}
