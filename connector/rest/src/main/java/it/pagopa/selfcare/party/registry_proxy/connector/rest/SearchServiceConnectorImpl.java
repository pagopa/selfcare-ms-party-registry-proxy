package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import io.github.resilience4j.retry.annotation.Retry;
import it.pagopa.selfcare.party.registry_proxy.connector.api.SearchServiceConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.*;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.AzureSearchRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.Const.*;

@Slf4j
@Service
public class SearchServiceConnectorImpl implements SearchServiceConnector {

  private final AzureSearchRestClient azureSearchRestClient;
  private static final int AZURE_BATCH_SIZE = 1000;

  public SearchServiceConnectorImpl(AzureSearchRestClient azureSearchRestClient) {
    this.azureSearchRestClient = azureSearchRestClient;
  }

  @Override
  public List<SearchServiceInstitution> findInstitutionIPAById(String id) {
    SearchServiceRequestBody request = SearchServiceRequestBody.builder().filter("id eq '" + id + "'").build();
    SearchServiceResponse searchServiceResponse = azureSearchRestClient.searchWithBody(AR_INDEX_NAME, INDEX_API_VERSION, request);
    List<SearchServiceInstitution> institutions = new ArrayList<>();
    Optional.of(searchServiceResponse).ifPresent(response -> institutions.addAll(response.getValue().stream()
            .map(SearchServiceInstitution::createSearchServiceInstitution)
            .toList()));
    return institutions;
  }

  @Override
  public SearchServiceStatus indexInstitution(Institution institution) {
    SearchServiceInstitutionRequest searchServiceInstitutionRequest = SearchServiceInstitutionRequest.createFromInstitution(institution);
    return azureSearchRestClient.indexInstitution(SearchServiceRequest.createFromInstitution(searchServiceInstitutionRequest), AR_INDEX_NAME, INDEX_API_VERSION);
  }

  @Override
  public void deleteIndex(String indexName, String apiVersion) {
    azureSearchRestClient.deleteIndex(indexName, apiVersion);
  }

  @Override
  public void createIndex(String indexName, String apiVersion, SearchIndexDefinition indexDefinition) {
    azureSearchRestClient.createOrUpdateIndex(indexName, indexDefinition, apiVersion);
  }

  @Override
  public SearchServiceStatus indexInstitutionsIPA(List<it.pagopa.selfcare.party.registry_proxy.connector.model.Institution> institutions) {

    List<List<it.pagopa.selfcare.party.registry_proxy.connector.model.Institution>> batches = partition(institutions);
    SearchServiceStatus lastStatus = null;

    for (List<it.pagopa.selfcare.party.registry_proxy.connector.model.Institution> batch : batches) {

      SearchServiceIPARequest request = new SearchServiceIPARequest();
      request.setValue(SearchServiceInstitutionIPARequest.createFromInstitutions(batch));

      SearchServiceStatus status =
              azureSearchRestClient.indexInstitutionIPA(
                      request,
                      IPA_INDEX_NAME,
                      INDEX_API_VERSION
              );

      if (!isBatchSuccessful(status)) {
        throw new IllegalStateException("Indexing batch failed: " + status);
      }
      lastStatus = status;
    }
    return lastStatus;
  }

  @Override
  @Retry(name = "retryServiceUnavailable")
  public List<SearchServiceInstitution> searchInstitution(String search, String filter, List<String> products, Integer top, Integer skip, String select, String orderby) {
    List<String> enabledProducts = Objects.isNull(products) ? List.of() : products;
    SearchServiceResponse searchServiceResponse = azureSearchRestClient.searchInstitution(search, filter, top, skip, select, orderby);
    List<SearchServiceInstitution> institutions = new ArrayList<>();
    Optional.of(searchServiceResponse).ifPresent(response -> institutions.addAll(response.getValue().stream()
            .map(SearchServiceInstitution::createSearchServiceInstitution)
            .map(searchServiceInstitution -> enabledProducts.contains("all") ? searchServiceInstitution : searchServiceInstitution.updateProductsEnable(enabledProducts))
            .toList()));
    return institutions;
  }

  @Override
  public List<SearchServiceInstitutionIPA> searchInstitutionFromIPA(String search, String filter, Integer top, Integer skip) {
    AISearchServiceResponse<SearchServiceInstitutionIPAResponse> searchServiceResponse = azureSearchRestClient.searchInstitutionFromIPA(search, filter, top, skip);
    List<SearchServiceInstitutionIPA> institutions = new ArrayList<>();
    Optional.of(searchServiceResponse).ifPresent(response -> institutions.addAll(response.getValue().stream()
            .map(SearchServiceInstitutionIPA::createSearchServiceInstitution)
            .toList()));
    return institutions;
  }

  private <T> List<List<T>> partition(List<T> list) {
    int size = AZURE_BATCH_SIZE;
    List<List<T>> partitions = new ArrayList<>();
    for (int i = 0; i < list.size(); i += size) {
      partitions.add(list.subList(i, Math.min(i + size, list.size())));
    }
    return partitions;
  }

  private boolean isBatchSuccessful(SearchServiceStatus status) {
    return status != null
            && status.getValue() != null
            && status.getValue().stream().allMatch(AzureSearchValue::getStatus);
  }
}
