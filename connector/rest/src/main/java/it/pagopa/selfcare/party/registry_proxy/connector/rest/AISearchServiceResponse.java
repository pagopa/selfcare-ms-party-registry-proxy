package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.SearchServiceInstitutionResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AISearchServiceResponse<T> {
  @JsonProperty("@odata.context")
  private String odataContext;

  @JsonProperty("@odata.count")
  private Long odataCount;

  private List<T> value;

}
