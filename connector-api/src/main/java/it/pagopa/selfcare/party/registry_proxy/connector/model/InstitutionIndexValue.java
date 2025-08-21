package it.pagopa.selfcare.party.registry_proxy.connector.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class InstitutionIndexValue {
  List<InstitutionIndex> value;

  public static InstitutionIndexValue createFromInstitution(InstitutionIndex institutionIndex) {
    InstitutionIndexValue institutionIndexValue = new InstitutionIndexValue();
    institutionIndexValue.setValue(new ArrayList<>());
    institutionIndexValue.getValue().add(institutionIndex);
    return institutionIndexValue;
  }
}
