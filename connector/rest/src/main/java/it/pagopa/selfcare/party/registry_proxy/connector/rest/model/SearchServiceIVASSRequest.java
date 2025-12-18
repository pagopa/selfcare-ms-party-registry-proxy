package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchServiceIVASSRequest {
  List<SearchServiceInsuranceCompanyIVASSRequest> value;

  public static SearchServiceIVASSRequest createFromInsuranceCompany(SearchServiceInsuranceCompanyIVASSRequest searchServiceInsuranceCompanyRequest) {
    SearchServiceIVASSRequest searchServiceRequest = new SearchServiceIVASSRequest();
    searchServiceRequest.setValue(new ArrayList<>());
    searchServiceRequest.getValue().add(searchServiceInsuranceCompanyRequest);
    return searchServiceRequest;
  }
}