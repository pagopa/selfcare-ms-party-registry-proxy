package it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Entity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

import java.util.function.Function;

import static it.pagopa.selfcare.party.registry_proxy.connector.model.Category.Field.ID;
import static it.pagopa.selfcare.party.registry_proxy.connector.model.UO.Field.*;

@Slf4j
public class UOToDocumentConverter implements Function<UO, Document> {

    @Override
    public Document apply(UO uo) {
        Document doc = null;
        if (uo != null) {
            doc = new Document();
            doc.add(new StringField(Entity.ENTITY_TYPE.toString(), Entity.UO.toString(), Field.Store.YES));
            doc.add(new StringField(ID.toString(), uo.getId(), Field.Store.YES));
            doc.add(new StringField(CODICE_IPA.toString(), uo.getCodiceIpa(), Field.Store.YES));
            doc.add(new StringField(CODICE_FISCALE_ENTE.toString(), uo.getCodiceFiscaleEnte(), Field.Store.YES));
            doc.add(new StringField(CODICE_UNI_UO.toString(), uo.getCodiceUniUo(), Field.Store.YES));
            doc.add(new StringField(CODICE_UNI_UO_PADRE.toString(), uo.getCodiceUniUoPadre(), Field.Store.YES));
            doc.add(new StringField(CODICE_UNI_AOO.toString(), uo.getCodiceUniAoo(), Field.Store.YES));
            doc.add(new StringField(ORIGIN.toString(), uo.getOrigin().toString(), Field.Store.YES));

            doc.add(new StringField(DENOMINAZIONE_ENTE.toString(), uo.getDenominazioneEnte(), Field.Store.YES));
            doc.add(new StringField(DESCRIZIONE_UO.toString(), uo.getDescrizioneUo(), Field.Store.YES));
            doc.add(new StringField(MAIL1.toString(), uo.getMail1(), Field.Store.YES));
        }
        return doc;
    }

}
