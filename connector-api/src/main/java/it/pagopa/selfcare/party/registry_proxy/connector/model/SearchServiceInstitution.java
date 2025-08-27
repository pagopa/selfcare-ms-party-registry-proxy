package it.pagopa.selfcare.party.registry_proxy.connector.model;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class SearchServiceInstitution {
  String id;
  String description;
  String taxCode;
  List<String> products;
  List<String> institutionTypes;
  OffsetDateTime lastModified;

  public SearchServiceInstitution createSearchServiceInstitution() {
    SearchServiceInstitution searchServiceInstitution = new SearchServiceInstitution();
    searchServiceInstitution.setId(id);
    searchServiceInstitution.setDescription(description);
    searchServiceInstitution.setTaxCode(taxCode);
    searchServiceInstitution.setProducts(products);
    searchServiceInstitution.setInstitutionTypes(institutionTypes);
    searchServiceInstitution.setLastModified(lastModified);
    return searchServiceInstitution;
  }
}
