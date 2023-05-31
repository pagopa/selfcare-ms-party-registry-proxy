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
            doc.add(new StringField(ID.toString(), aoo.getId().toUpperCase(), Field.Store.YES));
            doc.add(new StringField(ORIGIN.toString(), aoo.getOrigin().toString(), Field.Store.YES));
            doc.add(new StringField(CODICE_IPA.toString(), aoo.getCodiceIpa(), Field.Store.YES));
            doc.add(new StringField(CODICE_FISCALE_ENTE.toString(), aoo.getCodiceFiscaleEnte(), Field.Store.YES));
            doc.add(new StringField(CODICE_UNI_AOO.toString(), aoo.getCodiceUniAoo(), Field.Store.YES));
            doc.add(new StringField(COD_AOO.toString(), aoo.getCodAoo(), Field.Store.YES));

            doc.add(new StringField(DENOMINAZIONE_ENTE.toString(), aoo.getDenominazioneEnte(), Field.Store.YES));
            doc.add(new StringField(DENOMINAZIONE_AOO.toString(), aoo.getDenominazioneAoo(), Field.Store.YES));
            doc.add(new StringField(MAIL1.toString(), aoo.getMail1(), Field.Store.YES));

            doc.add(new StringField(DATA_ISTITUTIONE.toString(), aoo.getDataIstituzione(), Field.Store.YES));
            doc.add(new StringField(NOME_RESPONSABILE.toString(), aoo.getNomeResponsabile(), Field.Store.YES));
            doc.add(new StringField(COGNOME_RESPONSABILE.toString(), aoo.getCognomeResponsabile(), Field.Store.YES));
            doc.add(new StringField(MAIL_RESPONSABILE.toString(), aoo.getMailResponsabile(), Field.Store.YES));
            doc.add(new StringField(TELEFONO_RESPONSABILE.toString(), aoo.getTelefonoResponsabile(), Field.Store.YES));
            doc.add(new StringField(CODICE_COMUNE_ISTAT.toString(), aoo.getCodiceComuneISTAT(), Field.Store.YES));
            doc.add(new StringField(CODICE_CATASTALE_COMUNE.toString(), aoo.getCodiceCatastaleComune(), Field.Store.YES));
            doc.add(new StringField(CAP.toString(), aoo.getCAP(), Field.Store.YES));
            doc.add(new StringField(INDIRIZZO.toString(), aoo.getIndirizzo(), Field.Store.YES));
            doc.add(new StringField(TELEFONO.toString(), aoo.getTelefono(), Field.Store.YES));
            doc.add(new StringField(FAX.toString(), aoo.getFax(), Field.Store.YES));

            doc.add(new StringField(TIPO_MAIL1.toString(), aoo.getTipoMail1(), Field.Store.YES));
            doc.add(new StringField(PROTOCOLLO_INFORMATICO.toString(), aoo.getProtocolloInformatico(), Field.Store.YES));
            doc.add(new StringField(URI_PROTOCOLLO_INFORMATICO.toString(), aoo.getURIProtocolloInformatico(), Field.Store.YES));
            doc.add(new StringField(DATA_AGGIORNAMENTO.toString(), aoo.getDataAggiornamento(), Field.Store.YES));

        }
        return doc;
    }

}
