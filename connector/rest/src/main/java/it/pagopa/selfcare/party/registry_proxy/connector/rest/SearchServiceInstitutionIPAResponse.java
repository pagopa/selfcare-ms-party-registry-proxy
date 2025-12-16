package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchServiceInstitution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchServiceInstitutionIPA;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchServiceInstitutionIPAResponse extends SearchServiceInstitutionIPA {
  @JsonProperty("@search.score")
  private Double searchScore;
}
