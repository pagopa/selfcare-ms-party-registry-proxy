package it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.*;
import org.apache.lucene.util.BytesRef;

import java.util.function.Function;

import static it.pagopa.selfcare.party.registry_proxy.connector.model.Institution.Field.*;

@Slf4j
public class InstitutionToDocumentConverter implements Function<Institution, Document> {

    @Override
    public Document apply(Institution institution) {
        Document doc = null;
        if (institution != null) {
            doc = new Document();
            doc.add(new StringField(ID.toString(), institution.getId(), Field.Store.YES));
            doc.add(new StoredField(ORIGIN_ID.toString(), institution.getOriginId()));
            doc.add(new SortedDocValuesField(DESCRIPTION.toString(), new BytesRef(institution.getDescription())));
            doc.add(new TextField(DESCRIPTION.toString(), institution.getDescription(), Field.Store.YES));
            doc.add(new StoredField(TAX_CODE.toString(), institution.getTaxCode()));
            doc.add(new StoredField(CATEGORY.toString(), institution.getCategory()));
            doc.add(new StoredField(DIGITAL_ADDRESS.toString(), institution.getDigitalAddress()));
            doc.add(new StoredField(ADDRESS.toString(), institution.getAddress()));
            doc.add(new StoredField(ZIP_CODE.toString(), institution.getZipCode()));
            doc.add(new StoredField(ORIGIN.toString(), institution.getOrigin().toString()));
            if (institution.getO() != null) {
                doc.add(new StoredField(O.toString(), institution.getO()));
            }
            if (institution.getOu() != null) {
                doc.add(new StoredField(OU.toString(), institution.getOu()));
            }
            if (institution.getAoo() != null) {
                doc.add(new StoredField(AOO.toString(), institution.getAoo()));
            }
        }
        return doc;
    }

}
