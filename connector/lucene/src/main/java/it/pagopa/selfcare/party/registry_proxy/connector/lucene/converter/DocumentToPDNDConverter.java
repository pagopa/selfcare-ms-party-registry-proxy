package it.pagopa.selfcare.party.registry_proxy.connector.lucene.converter;

import it.pagopa.selfcare.party.registry_proxy.connector.lucene.model.PDNDEntity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.PDND;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;

import java.util.function.Function;

import static it.pagopa.selfcare.party.registry_proxy.connector.model.Institution.Field.*;

@Slf4j
public class DocumentToPDNDConverter implements Function<Document, PDND> {

    @Override
    public PDND apply(Document document) {
        PDNDEntity pdnd = null;
        if (document != null) {
            pdnd = new PDNDEntity();
            pdnd.setId(document.get(ID.toString()));
            pdnd.setOriginId(document.get(ORIGIN_ID.toString()));
            pdnd.setTaxCode(document.get(TAX_CODE.toString()));
            pdnd.setDescription(document.get(DESCRIPTION.toString()));
            pdnd.setDigitalAddress(document.get(DIGITAL_ADDRESS.toString()));
        }
        return pdnd;
    }

}
