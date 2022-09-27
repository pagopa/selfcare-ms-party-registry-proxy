package it.pagopa.selfcare.party.registry_proxy.connector.lucene;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import org.apache.lucene.document.Document;

public class InstitutionDocumentConverter implements DocumentConverter<Institution> {

    @Override
    public Institution to(Document document) {
        return new Institution() {
            @Override
            public String getId() {
                return document.get("id");
            }

            @Override
            public String getOriginId() {
                return document.get("originId");
            }

            @Override
            public String getO() {
                return document.get("o");
            }

            @Override
            public String getOu() {
                return document.get("ou");
            }

            @Override
            public String getAoo() {
                return document.get("aoo");
            }

            @Override
            public String getTaxCode() {
                return document.get("taxCode");
            }

            @Override
            public String getCategory() {
                return document.get("category");
            }

            @Override
            public String getDescription() {
                return document.get("description");
            }

            @Override
            public String getDigitalAddress() {
                return document.get("digitalAddress");
            }

            @Override
            public String getAddress() {
                return document.get("address");
            }

            @Override
            public String getZipCode() {
                return document.get("zipCode");
            }

            @Override
            public Origin getOrigin() {
                return Origin.valueOf(document.get("origin"));
            }
        };
    }
}
