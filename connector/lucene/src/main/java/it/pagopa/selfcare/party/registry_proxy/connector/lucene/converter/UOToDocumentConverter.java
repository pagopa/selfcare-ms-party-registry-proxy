package it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Entity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

import java.util.Objects;
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
            doc.add(new StringField(ID.toString(), uo.getId() != null ? uo.getId().toUpperCase() : uo.getId(), Field.Store.YES));
            doc.add(new StringField(CODICE_IPA.toString(), uo.getCodiceIpa(), Field.Store.YES));
            doc.add(new StringField(CODICE_FISCALE_ENTE.toString(), uo.getCodiceFiscaleEnte(), Field.Store.YES));
            doc.add(new StringField(CODICE_UNI_UO.toString(), uo.getCodiceUniUo(), Field.Store.YES));
            if (Objects.nonNull(uo.getCodiceFiscaleSfe())) {
                doc.add(new StringField(CODICE_FISCALE_SFE.toString(), uo.getCodiceFiscaleSfe(), Field.Store.YES));
            }
            doc.add(new StringField(CODICE_UNI_UO_PADRE.toString(), uo.getCodiceUniUoPadre(), Field.Store.YES));
            doc.add(new StringField(CODICE_UNI_AOO.toString(), uo.getCodiceUniAoo(), Field.Store.YES));
            doc.add(new StringField(ORIGIN.toString(), uo.getOrigin().toString(), Field.Store.YES));

            doc.add(new StringField(DENOMINAZIONE_ENTE.toString(), uo.getDenominazioneEnte(), Field.Store.YES));
            doc.add(new StringField(DESCRIZIONE_UO.toString(), uo.getDescrizioneUo(), Field.Store.YES));
            doc.add(new StringField(MAIL1.toString(), uo.getMail1(), Field.Store.YES));

            doc.add(new StringField(DATA_ISTITUTIONE.toString(), uo.getDataIstituzione(), Field.Store.YES));
            doc.add(new StringField(NOME_RESPONSABILE.toString(), uo.getNomeResponsabile(), Field.Store.YES));
            doc.add(new StringField(COGNOME_RESPONSABILE.toString(), uo.getCognomeResponsabile(), Field.Store.YES));
            doc.add(new StringField(MAIL_RESPONSABILE.toString(), uo.getMailResponsabile(), Field.Store.YES));
            doc.add(new StringField(TELEFONO_RESPONSABILE.toString(), uo.getTelefonoResponsabile(), Field.Store.YES));
            doc.add(new StringField(CODICE_COMUNE_ISTAT.toString(), uo.getCodiceComuneISTAT(), Field.Store.YES));
            doc.add(new StringField(CODICE_CATASTALE_COMUNE.toString(), uo.getCodiceCatastaleComune(), Field.Store.YES));
            doc.add(new StringField(CAP.toString(), uo.getCAP(), Field.Store.YES));
            doc.add(new StringField(INDIRIZZO.toString(), uo.getIndirizzo(), Field.Store.YES));
            doc.add(new StringField(TELEFONO.toString(), uo.getTelefono(), Field.Store.YES));
            doc.add(new StringField(FAX.toString(), uo.getFax(), Field.Store.YES));
            doc.add(new StringField(TIPO_MAIL1.toString(), uo.getTipoMail1(), Field.Store.YES));
            doc.add(new StringField(URL.toString(), uo.getUrl(), Field.Store.YES));
            doc.add(new StringField(DATA_AGGIORNAMENTO.toString(), uo.getDataAggiornamento(), Field.Store.YES));

        }
        return doc;
    }

}
