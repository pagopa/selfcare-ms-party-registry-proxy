package it.pagopa.selfcare.party.registry_proxy.connector.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Onboarding;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class InstitutionIndex {
  @JsonProperty("@search.action")
  String action;
  String id;
  String description;
  String taxCode;
  List<String> products;
  List<String> institutionTypes;
  OffsetDateTime lastModified;

  public static InstitutionIndex createFromInstitution(Institution institution) {
    InstitutionIndex institutionIndex = new InstitutionIndex();
    institutionIndex.setId(institution.getId());
    institutionIndex.setDescription(institution.getDescription());
    institutionIndex.setTaxCode(institution.getTaxCode());
    institutionIndex.setProducts(institution.getOnboarding().stream().map(Onboarding::getProductId).toList());
    institutionIndex.setInstitutionTypes(institution.getOnboarding().stream().map(onboarding -> onboarding.getInstitutionType().toString()).collect(Collectors.toSet()).stream().toList());
    institutionIndex.setLastModified(institution.getUpdatedAt());
    institutionIndex.setAction("mergeOrUpload");
    return institutionIndex;
  }
}
