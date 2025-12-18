package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.api.SearchIvassServiceConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.*;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.IvassAzureSearchRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.*;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.AzureUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.Const.*;

@Slf4j
@Service
public class SearchIvassServiceConnectorImpl implements SearchIvassServiceConnector {

  private final IvassAzureSearchRestClient azureSearchRestClient;

  public SearchIvassServiceConnectorImpl(IvassAzureSearchRestClient azureSearchRestClient) {
    this.azureSearchRestClient = azureSearchRestClient;
  }

  @Override
  public SearchServiceStatus indexIVASS(List<InsuranceCompany> filteredCompanies) {
    List<List<InsuranceCompany>> batches = AzureUtils.partition(filteredCompanies);
    SearchServiceStatus lastStatus = null;

    for (List<InsuranceCompany> batch : batches) {

      SearchServiceIVASSRequest request = new SearchServiceIVASSRequest();
      request.setValue(SearchServiceInsuranceCompanyIVASSRequest.createFromInsuranceCompanies(batch));

      SearchServiceStatus status =
              azureSearchRestClient.indexInsuranceCompanyIVASS(
                      request,
                      IVASS_INDEX_NAME,
                      INDEX_API_VERSION
              );

      if (!AzureUtils.isBatchSuccessful(status)) {
        log.error("Indexing batch failed: " + status);
      }
      lastStatus = status;
    }
    return lastStatus;
  }
}
