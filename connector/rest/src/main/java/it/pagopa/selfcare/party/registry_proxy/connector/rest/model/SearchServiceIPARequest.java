package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class SearchServiceIPARequest {
  List<SearchServiceInstitutionIPARequest> value;

  public static SearchServiceIPARequest createFromInstitution(SearchServiceInstitutionIPARequest searchServiceInstitutionRequest) {
    SearchServiceIPARequest searchServiceRequest = new SearchServiceIPARequest();
    searchServiceRequest.setValue(new ArrayList<>());
    searchServiceRequest.getValue().add(searchServiceInstitutionRequest);
    return searchServiceRequest;
  }
}
