package it.pagopa.selfcare.party.registry_proxy.connector.lucene;

import org.apache.lucene.document.Document;

public interface DocumentConverter<T> {

    T to(Document document);

}
