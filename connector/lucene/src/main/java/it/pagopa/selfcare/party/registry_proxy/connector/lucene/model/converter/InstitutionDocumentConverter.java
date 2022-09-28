package it.pagopa.selfcare.party.registry_proxy.connector.lucene.model.converter;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.model.InstitutionEntity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.util.BytesRef;
import org.springframework.stereotype.Service;

import static it.pagopa.selfcare.party.registry_proxy.connector.model.Institution.Field.*;

@Slf4j
@Service
class InstitutionDocumentConverter implements DocumentConverter<Institution> {

    @Override
    public Institution to(Document document) {
        InstitutionEntity institution = null;
        if (document != null) {
            institution = new InstitutionEntity();
            institution.setId(document.get(ID.toString()));
            institution.setOriginId(document.get(ORIGIN_ID.toString()));
            institution.setO(document.get(O.toString()));
            institution.setOu(document.get(OU.toString()));
            institution.setAoo(document.get(AOO.toString()));
            institution.setTaxCode(document.get(TAX_CODE.toString()));
            institution.setCategory(document.get(CATEGORY.toString()));
            institution.setDescription(document.get(DESCRIPTION.toString()));
            institution.setDigitalAddress(document.get(DIGITAL_ADDRESS.toString()));
            institution.setAddress(document.get(ADDRESS.toString()));
            institution.setZipCode(document.get(ZIP_CODE.toString()));
            institution.setOrigin(Origin.valueOf(document.get(ORIGIN.toString())));
        }
        return institution;
    }


    @Override
    public Document from(Institution item) {
        Document doc = null;
        if (item != null) {
            doc = new Document();
            doc.add(new StringField(ID.toString(), item.getId(), Store.YES));
            doc.add(new StoredField(ORIGIN_ID.toString(), item.getOriginId()));
            doc.add(new SortedDocValuesField(DESCRIPTION.toString(), new BytesRef(item.getDescription())));
            doc.add(new TextField(DESCRIPTION.toString(), item.getDescription(), Store.YES));
            doc.add(new StoredField(TAX_CODE.toString(), item.getTaxCode()));
            doc.add(new StoredField(CATEGORY.toString(), item.getCategory()));
            doc.add(new StoredField(DIGITAL_ADDRESS.toString(), item.getDigitalAddress()));
            doc.add(new StoredField(ADDRESS.toString(), item.getAddress()));
            doc.add(new StoredField(ZIP_CODE.toString(), item.getZipCode()));
            doc.add(new StoredField(ORIGIN.toString(), item.getOrigin().toString()));

            if (item.getO() != null) {
                doc.add(new StoredField(O.toString(), item.getO()));
            }
            if (item.getOu() != null) {
                doc.add(new StoredField(OU.toString(), item.getOu()));
            }
            if (item.getAoo() != null) {
                doc.add(new StoredField(AOO.toString(), item.getAoo()));
            }
        }
        return doc;
    }

}
