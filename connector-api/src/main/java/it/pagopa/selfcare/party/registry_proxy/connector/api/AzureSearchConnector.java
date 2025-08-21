package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.AzureSearchStatus;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Institution;

public interface AzureSearchConnector {
  AzureSearchStatus indexInstitution(Institution institution);
}
