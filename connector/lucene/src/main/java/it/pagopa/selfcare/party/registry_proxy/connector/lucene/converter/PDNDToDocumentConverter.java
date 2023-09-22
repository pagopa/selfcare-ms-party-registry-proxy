package it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Entity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.PDND;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.*;
import org.apache.lucene.util.BytesRef;

import java.util.function.Function;

import static it.pagopa.selfcare.party.registry_proxy.connector.model.PDND.Field.*;

@Slf4j
public class PDNDToDocumentConverter implements Function<PDND, Document> {

    @Override
    public Document apply(PDND pdnd) {
        Document doc = null;
        if (pdnd != null) {
            doc = new Document();
            doc.add(new StringField(Entity.ENTITY_TYPE.toString(), Entity.PDND.toString(), Field.Store.YES));
            doc.add(new StringField(ID.toString(), pdnd.getId(), Field.Store.YES));
            doc.add(new StoredField(ORIGIN_ID.toString(), pdnd.getOriginId()));
            doc.add(new SortedDocValuesField(DESCRIPTION.toString(), new BytesRef(pdnd.getDescription())));
            doc.add(new TextField(DESCRIPTION.toString(), pdnd.getDescription(), Field.Store.YES));
            doc.add(new StoredField(TAX_CODE.toString(), pdnd.getTaxCode()));
            doc.add(new StoredField(DIGITAL_ADDRESS.toString(), pdnd.getDigitalAddress()));
            doc.add(new StoredField(ANAC_ENABLED.toString(), String.valueOf(pdnd.isAnacEnabled())));
            doc.add(new StoredField(ANAC_ENGAGED.toString(), String.valueOf(pdnd.isAnacEngaged())));
        }
        return doc;
    }

}
