package it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.model.UOEntity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;

import java.util.function.Function;

import static it.pagopa.selfcare.party.registry_proxy.connector.model.UO.Field.*;

@Slf4j
public class DocumentToUOConverter implements Function<Document, UO> {

    @Override
    public UO apply(Document document) {
        UOEntity entity = null;
        if (document != null) {
            entity = new UOEntity();
            entity.setCodiceIpa(document.get(CODICE_IPA.toString()));
            entity.setOrigin(Origin.fromValue(document.get(ORIGIN.toString())));
            entity.setDenominazioneEnte(document.get(DENOMINAZIONE_ENTE.toString()));
            entity.setCodiceFiscaleEnte(document.get(CODICE_FISCALE_ENTE.toString()));
            entity.setCodiceUniAoo(document.get(CODICE_UNI_AOO.toString()));
            entity.setCodiceUniUo(document.get(CODICE_UNI_UO.toString()));
            entity.setCodiceUniUoPadre(document.get(CODICE_UNI_UO_PADRE.toString()));
            entity.setMail1(document.get(MAIL1.toString()));
            entity.setDescrizioneUo(document.get(DESCRIZIONE_UO.toString()));
        }
        return entity;
    }

}
