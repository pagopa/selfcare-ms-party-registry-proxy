package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import io.github.resilience4j.retry.annotation.Retry;
import it.pagopa.selfcare.party.registry_proxy.connector.api.AzureSearchConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AzureSearchStatus;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InstitutionIndex;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InstitutionIndexValue;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.AzureSearchRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AzureSearchConnectorImpl implements AzureSearchConnector {

  private final AzureSearchRestClient azureSearchRestClient;

  public AzureSearchConnectorImpl(AzureSearchRestClient azureSearchRestClient) {
    this.azureSearchRestClient = azureSearchRestClient;
  }

  @Override
  @Retry(name = "retryServiceUnavailable")
  public AzureSearchStatus indexInstitution(Institution institution) {
    InstitutionIndex institutionIndex = InstitutionIndex.createFromInstitution(institution);
    return azureSearchRestClient.indexInstitution(InstitutionIndexValue.createFromInstitution(institutionIndex));
  }
}
