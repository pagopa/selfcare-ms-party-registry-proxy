package it.pagopa.selfcare.party.registry_proxy.core;

import java.util.List;
import java.util.Map;

public interface SearchService {

  List<Map<String, String>> subscribe();
  boolean indexInstitution(String institutionId);
}
