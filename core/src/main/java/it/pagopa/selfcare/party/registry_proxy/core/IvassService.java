package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;

public interface IvassService {
    InsuranceCompany findByTaxCode(String taxId);
}

