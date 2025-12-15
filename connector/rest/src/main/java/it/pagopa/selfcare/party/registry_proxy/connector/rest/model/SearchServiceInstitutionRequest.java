package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchServiceInstitution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Onboarding;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class SearchServiceInstitutionRequest extends SearchServiceInstitution {
  @JsonProperty("@search.action")
  String action;

  public static SearchServiceInstitutionRequest createFromInstitution(Institution institution) {
    SearchServiceInstitutionRequest searchServiceInstitutionRequest = new SearchServiceInstitutionRequest();
    searchServiceInstitutionRequest.setId(institution.getId());
    searchServiceInstitutionRequest.setDescription(institution.getDescription());
    searchServiceInstitutionRequest.setParentDescription(institution.getParentDescription());
    searchServiceInstitutionRequest.setTaxCode(institution.getTaxCode());
    searchServiceInstitutionRequest.setProducts(institution.getOnboarding().stream().map(Onboarding::getProductId).toList());
    searchServiceInstitutionRequest.setInstitutionTypes(institution.getOnboarding().stream().map(Onboarding::getInstitutionType).collect(Collectors.toSet()).stream().toList());
    searchServiceInstitutionRequest.setLastModified(institution.getUpdatedAt());
    searchServiceInstitutionRequest.setAction("mergeOrUpload");
    return searchServiceInstitutionRequest;
  }

  public static SearchServiceInstitutionRequest createFromInstitutionIPA(it.pagopa.selfcare.party.registry_proxy.connector.model.Institution institution) {
    SearchServiceInstitutionRequest searchServiceInstitutionRequest = new SearchServiceInstitutionRequest();
    searchServiceInstitutionRequest.setId(institution.getId());
    searchServiceInstitutionRequest.setDescription(institution.getDescription());
    searchServiceInstitutionRequest.setTaxCode(institution.getTaxCode());
    searchServiceInstitutionRequest.setAction("mergeOrUpload");
    return searchServiceInstitutionRequest;
  }

  public static List<SearchServiceInstitutionRequest> createFromInstitutions(List<it.pagopa.selfcare.party.registry_proxy.connector.model.Institution> institutions) {
    List<SearchServiceInstitutionRequest> requestList = new ArrayList<>();
    institutions.stream().forEach(institution -> {
      SearchServiceInstitutionRequest searchServiceInstitutionRequest = createFromInstitutionIPA(institution);
      requestList.add(searchServiceInstitutionRequest);
    });
    return requestList;
  }


}
