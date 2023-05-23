package it.pagopa.selfcare.party.registry_proxy.connector.lucene.model;

import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.springframework.test.util.ReflectionTestUtils.invokeGetterMethod;
import static org.springframework.util.StringUtils.capitalize;

public class DummyAOO implements AOO {

    private final AOOEntity dummyEntity = mockInstance(new AOOEntity());


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
    public String getCodiceIpa() {
        return dummyEntity.getCodiceIpa();
    }

    @Override
    public String getDenominazioneEnte() {
        return dummyEntity.getDenominazioneEnte();
    }

    @Override
    public String getCodiceFiscaleEnte() {
        return dummyEntity.getCodiceFiscaleEnte();
    }

    @Override
    public String getCodiceUniAoo() {
        return dummyEntity.getCodiceUniAoo();
    }

    @Override
    public String getDenominazioneAoo() {
        return dummyEntity.getDenominazioneAoo();
    }

    @Override
    public String getMail1() {
        return dummyEntity.getMail1();
    }

    @Override
    public String getCodAoo() {
        return dummyEntity.getCodAoo();
    }

    @Override
    public Origin getOrigin() {
        return dummyEntity.getOrigin();
    }
}