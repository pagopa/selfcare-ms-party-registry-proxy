package it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.model.AOOEntity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;

import java.util.function.Function;

import static it.pagopa.selfcare.party.registry_proxy.connector.model.AOO.Field.*;

@Slf4j
public class DocumentToAOOConverter implements Function<Document, AOO> {

    @Override
    public AOO apply(Document document) {
        AOOEntity entity = null;
        if (document != null) {
            entity = new AOOEntity();
            entity.setCodiceIpa(document.get(CODICE_IPA.toString()));
            entity.setOrigin(Origin.fromValue(document.get(ORIGIN.toString())));
            entity.setDenominazioneEnte(document.get(DENOMINAZIONE_ENTE.toString()));
            entity.setCodiceFiscaleEnte(document.get(CODICE_FISCALE_ENTE.toString()));
            entity.setCodiceUniAoo(document.get(CODICE_UNI_AOO.toString()));
            entity.setDenominazioneAoo(document.get(DENOMINAZIONE_AOO.toString()));
            entity.setMail1(document.get(MAIL1.toString()));
            entity.setCodAoo(document.get(COD_AOO.toString()));
        }
        return entity;
    }

}
