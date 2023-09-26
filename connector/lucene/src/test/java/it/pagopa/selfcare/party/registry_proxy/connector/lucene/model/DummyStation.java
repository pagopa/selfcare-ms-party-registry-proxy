package it.pagopa.selfcare.party.registry_proxy.connector.lucene.model;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.springframework.test.util.ReflectionTestUtils.invokeGetterMethod;
import static org.springframework.util.StringUtils.capitalize;

public class DummyStation implements Station {

    private final StationEntity dummyEntity = mockInstance(new StationEntity());

    public Document toDocument() {
        final Document doc = new Document();
        for (Field field : Field.values()) {
            final String value = String.valueOf(invokeGetterMethod(dummyEntity, "get" + capitalize(field.toString())));
            doc.add(new StringField(field.toString(), value, Store.NO));
        }
        return doc;
    }


    @Override
    public String getId() {
        return dummyEntity.getId();
    }

    @Override
    public String getTaxCode() {
        return dummyEntity.getTaxCode();
    }

    @Override
    public String getDescription() {
        return dummyEntity.getDescription();
    }

    @Override
    public String getDigitalAddress() {
        return dummyEntity.getDigitalAddress();
    }

    @Override
    public boolean isAnacEnabled() {
        return dummyEntity.isAnacEnabled();
    }

    @Override
    public boolean isAnacEngaged() {
        return dummyEntity.isAnacEngaged();
    }

    @Override
    public Origin getOrigin() {
        return dummyEntity.getOrigin();
    }

    @Override
    public String getOriginId() { return dummyEntity.getOriginId(); }

}