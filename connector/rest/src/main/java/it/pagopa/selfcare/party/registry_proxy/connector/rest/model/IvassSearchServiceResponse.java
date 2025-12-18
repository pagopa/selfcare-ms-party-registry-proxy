package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.SearchServiceInsuranceCompanyIVASSResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class IvassSearchServiceResponse {
  @JsonProperty("@odata.context")
  private String odataContext;

  @JsonProperty("@odata.count")
  private Long odataCount;

  private List<SearchServiceInsuranceCompanyIVASSResponse> value;

}