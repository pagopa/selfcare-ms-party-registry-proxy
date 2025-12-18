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
import java.util.function.BiFunction;
import java.util.function.Function;

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
  public List<SearchServiceInstitutionIPA> findInstitutionIPAById(String id, String indexName) {
    SearchServiceRequestBody request = SearchServiceRequestBody.builder().filter("id eq '" + id + "'").build();
    AISearchServiceResponse<SearchServiceInstitutionIPAResponse> searchServiceResponse = azureSearchRestClient.searchWithBody(indexName, INDEX_API_VERSION, request);
    List<SearchServiceInstitutionIPA> institutions = new ArrayList<>();
    Optional.of(searchServiceResponse).ifPresent(response -> institutions.addAll(response.getValue().stream()
            .map(SearchServiceInstitutionIPA::createSearchServiceInstitution)
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

    return indexInBatches(
            institutions,
            batch -> {
              SearchServiceIPARequest req = new SearchServiceIPARequest();
              req.setValue(
                      SearchServiceInstitutionIPARequest.createFromInstitutions(batch)
              );
              return req;
            },
            (req, index) -> azureSearchRestClient.indexInstitutionIPA(
                    req,
                    index,
                    INDEX_API_VERSION
            ),
            IPA_INDEX_NAME
    );
  }

  public <T, R> SearchServiceStatus indexInBatches(
          List<T> items,
          Function<List<T>, R> requestMapper,
          BiFunction<R, String, SearchServiceStatus> indexer,
          String indexName
  ) {
    List<List<T>> batches = partition(items);
    SearchServiceStatus lastStatus = null;

    for (List<T> batch : batches) {

      R request = requestMapper.apply(batch);

      SearchServiceStatus status =
              indexer.apply(request, indexName);

      if (!isBatchSuccessful(status)) {
        log.error("Indexing batch failed: {}", status);
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
    int skipSearch = (skip - 1) * top;
    AISearchServiceResponse<SearchServiceInstitutionIPAResponse> searchServiceResponse = azureSearchRestClient.searchInstitutionFromIPA(search, filter, top, skipSearch, true);
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
