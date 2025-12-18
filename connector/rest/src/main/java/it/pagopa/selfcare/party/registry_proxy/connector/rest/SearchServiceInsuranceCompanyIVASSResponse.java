package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchServiceInsuranceCompanyIVASS;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchServiceInsuranceCompanyIVASSResponse extends SearchServiceInsuranceCompanyIVASS {
  @JsonProperty("@search.score")
  private Double searchScore;
}
