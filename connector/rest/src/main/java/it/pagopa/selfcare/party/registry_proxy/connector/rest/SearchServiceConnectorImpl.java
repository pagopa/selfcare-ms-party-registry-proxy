package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import static it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.Const.AR_INDEX_NAME;
import static it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.Const.IPA_INDEX_NAME;

import io.github.resilience4j.retry.annotation.Retry;
import it.pagopa.selfcare.party.registry_proxy.connector.api.SearchServiceConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchServiceInstitution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchServiceStatus;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.AzureSearchRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SearchServiceConnectorImpl implements SearchServiceConnector {

  private final AzureSearchRestClient azureSearchRestClient;

  public SearchServiceConnectorImpl(AzureSearchRestClient azureSearchRestClient) {
    this.azureSearchRestClient = azureSearchRestClient;
  }

  @Override
  public List<SearchServiceInstitution> findInstitutionIPAById(String id) {
    SearchServiceRequestBody request = SearchServiceRequestBody.builder().filter("id eq '" + id + "'").build();
    SearchServiceResponse searchServiceResponse = azureSearchRestClient.searchWithBody(AR_INDEX_NAME, "1.0.0", request);
    List<SearchServiceInstitution> institutions = new ArrayList<>();
    Optional.of(searchServiceResponse).ifPresent(response -> institutions.addAll(response.getValue().stream()
            .map(SearchServiceInstitution::createSearchServiceInstitution)
            .toList()));
    return institutions;
  }

  @Override
  public SearchServiceStatus indexInstitution(Institution institution) {
    SearchServiceInstitutionRequest searchServiceInstitutionRequest = SearchServiceInstitutionRequest.createFromInstitution(institution);
    return azureSearchRestClient.indexInstitution(SearchServiceRequest.createFromInstitution(searchServiceInstitutionRequest), AR_INDEX_NAME);
  }

  @Override
  public void deleteIndex(String indexName, String apiVersion) {
      azureSearchRestClient.deleteIndex(indexName, apiVersion);
  }

  @Override
  public SearchServiceStatus indexInstitutionsIPA(List<it.pagopa.selfcare.party.registry_proxy.connector.model.Institution> institutions) {
    SearchServiceRequest request = new SearchServiceRequest();
    List<SearchServiceInstitutionRequest> list = SearchServiceInstitutionRequest.createFromInstitutions(institutions);
    request.setValue(list);
    return azureSearchRestClient.indexInstitution(request, IPA_INDEX_NAME);
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
  public List<SearchServiceInstitution> searchInstitutionFromIPA(String search, String filter, Integer top, Integer skip) {
    AISearchServiceResponse<SearchServiceInstitutionResponse> searchServiceResponse = azureSearchRestClient.searchInstitutionFromIPA(search, filter, top, skip);
    List<SearchServiceInstitution> institutions = new ArrayList<>();
    Optional.of(searchServiceResponse).ifPresent(response -> institutions.addAll(response.getValue().stream()
            .map(SearchServiceInstitution::createSearchServiceInstitution)
            .toList()));
    return institutions;
  }
}
