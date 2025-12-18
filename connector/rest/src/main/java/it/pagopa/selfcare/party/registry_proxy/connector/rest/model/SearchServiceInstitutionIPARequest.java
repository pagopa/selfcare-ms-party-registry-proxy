package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchServiceInstitutionIPA;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchServiceInstitutionIPARequest extends SearchServiceInstitutionIPA {
  @JsonProperty("@search.action")
  String action;

  public static SearchServiceInstitutionIPARequest createFromInstitutionIPA(it.pagopa.selfcare.party.registry_proxy.connector.model.Institution institution) {
    SearchServiceInstitutionIPARequest searchServiceInstitutionRequest = new SearchServiceInstitutionIPARequest();
    searchServiceInstitutionRequest.setId(institution.getId());
    searchServiceInstitutionRequest.setOrigin(institution.getOrigin().name());
    searchServiceInstitutionRequest.setOriginId(institution.getOriginId());
    searchServiceInstitutionRequest.setCategory(institution.getCategory());
    searchServiceInstitutionRequest.setDescription(institution.getDescription());
    searchServiceInstitutionRequest.setDescriptionFull(institution.getDescription());
    searchServiceInstitutionRequest.setIstatCode(institution.getIstatCode());
    searchServiceInstitutionRequest.setDigitalAddress(institution.getDigitalAddress());
    searchServiceInstitutionRequest.setTaxCode(institution.getTaxCode());
    searchServiceInstitutionRequest.setAoo(institution.getAoo());
    searchServiceInstitutionRequest.setUo(institution.getOu());
    searchServiceInstitutionRequest.setAddress(institution.getAddress());
    searchServiceInstitutionRequest.setZipCode(institution.getZipCode());
    searchServiceInstitutionRequest.setAction("mergeOrUpload");
    return searchServiceInstitutionRequest;
  }

  public static List<SearchServiceInstitutionIPARequest> createFromInstitutions(List<it.pagopa.selfcare.party.registry_proxy.connector.model.Institution> institutions) {
    List<SearchServiceInstitutionIPARequest> requestList = new ArrayList<>();
    institutions.stream().forEach(institution -> {
      SearchServiceInstitutionIPARequest searchServiceInstitutionRequest = createFromInstitutionIPA(institution);
      requestList.add(searchServiceInstitutionRequest);
    });
    return requestList;
  }


}
