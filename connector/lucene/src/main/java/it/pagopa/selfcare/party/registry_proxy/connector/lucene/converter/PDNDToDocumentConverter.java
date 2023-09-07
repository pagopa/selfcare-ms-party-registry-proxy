package it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Entity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.PDND;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.*;
import org.apache.lucene.util.BytesRef;
import org.springframework.util.StringUtils;

import java.util.function.Function;

import static it.pagopa.selfcare.party.registry_proxy.connector.model.Institution.Field.*;

@Slf4j
public class PDNDToDocumentConverter implements Function<PDND, Document> {

    @Override
    public Document apply(PDND institution) {
        Document doc = null;
        if (institution != null) {
            doc = new Document();
            doc.add(new StringField(Entity.ENTITY_TYPE.toString(), Entity.PDND.toString(), Field.Store.YES));
            doc.add(new StringField(ID.toString(), institution.getId(), Field.Store.YES));
            doc.add(new StoredField(ORIGIN_ID.toString(), institution.getOriginId()));
            doc.add(new SortedDocValuesField(DESCRIPTION.toString(), new BytesRef(institution.getDescription())));
            doc.add(new TextField(DESCRIPTION.toString(), institution.getDescription(), Field.Store.YES));
            doc.add(new StoredField(TAX_CODE.toString(), institution.getTaxCode()));
        }
        return doc;
    }

}
