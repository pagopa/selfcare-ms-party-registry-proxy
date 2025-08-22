package it.pagopa.selfcare.party.registry_proxy.connector.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class AzureSearchStatus {
  @JsonProperty("value")
  List<AzureSearchValue> value;
}
