package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchServiceStatus;

import java.util.List;

public interface SearchIvassServiceConnector {

    SearchServiceStatus indexIVASS(List<InsuranceCompany> filteredCompanies);
}
