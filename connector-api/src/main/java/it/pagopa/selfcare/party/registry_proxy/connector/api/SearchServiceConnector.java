package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.*;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Institution;

import java.util.List;

public interface SearchServiceConnector {
  List<SearchServiceInstitutionIPA> findInstitutionIPAById(String id, String indexName);
  SearchServiceStatus indexInstitution(Institution institution);
  void deleteIndex(String indexName, String apiVersion);
  void createIndex(String indexName, String apiVersion, SearchIndexDefinition indexDefinition);
  SearchServiceStatus indexInstitutionsIPA(List<it.pagopa.selfcare.party.registry_proxy.connector.model.Institution> institutions);
  List<SearchServiceInstitution> searchInstitution(String search, String filter, List<String> products, Integer top, Integer skip, String select, String orderby);
  List<SearchServiceInstitutionIPA> searchInstitutionFromIPA(String search, String filter, Integer top, Integer skip);
}
