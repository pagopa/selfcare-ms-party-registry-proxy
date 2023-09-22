package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;

public class MockOpenDataPDND extends OpenDataPDNDTemplate {

    @Override
    public Origin getOrigin() {
        return Origin.MOCK;
    }

}
