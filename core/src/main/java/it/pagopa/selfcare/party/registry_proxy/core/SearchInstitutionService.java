package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchServiceInstitution;
import java.util.List;
import java.util.Optional;

public interface SearchInstitutionService {

    List<SearchServiceInstitution> search(Optional<String> searchText, int page, int limit);

    List<SearchServiceInstitution> search(Optional<String> searchText, String categories, int page, int limit);

    Institution findById(String id, Optional<Origin> origin, List<String> categories);

}
