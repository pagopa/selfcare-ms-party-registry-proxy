package it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter;

import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Entity;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

import java.util.function.Function;

import static it.pagopa.selfcare.party.registry_proxy.connector.model.AOO.Field.*;
import static it.pagopa.selfcare.party.registry_proxy.connector.model.Category.Field.ID;

@Slf4j
public class AOOToDocumentConverter implements Function<AOO, Document> {

    @Override
    public Document apply(AOO aoo) {
        Document doc = null;
        if (aoo != null) {
            doc = new Document();
            doc.add(new StringField(Entity.ENTITY_TYPE.toString(), Entity.AOO.toString(), Field.Store.YES));
            doc.add(new StringField(ID.toString(), aoo.getId(), Field.Store.YES));
            doc.add(new StringField(ORIGIN.toString(), aoo.getOrigin().toString(), Field.Store.YES));
            doc.add(new StringField(CODICE_IPA.toString(), aoo.getCodiceIpa(), Field.Store.YES));
            doc.add(new StringField(CODICE_FISCALE_ENTE.toString(), aoo.getCodiceFiscaleEnte(), Field.Store.YES));
            doc.add(new StringField(CODICE_UNI_AOO.toString(), aoo.getCodiceUniAoo(), Field.Store.YES));
            doc.add(new StringField(COD_AOO.toString(), aoo.getCodAoo(), Field.Store.YES));

            doc.add(new StringField(DENOMINAZIONE_ENTE.toString(), aoo.getDenominazioneEnte(), Field.Store.YES));
            doc.add(new StringField(DENOMINAZIONE_AOO.toString(), aoo.getDenominazioneAoo(), Field.Store.YES));
            doc.add(new StringField(MAIL1.toString(), aoo.getMail1(), Field.Store.YES));
        }
        return doc;
    }

}
