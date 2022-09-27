package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.web.model.InstitutionResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.InstitutionsResource;

import java.util.List;

public class InstitutionsMapper {

    public static InstitutionsResource toResource(List<InstitutionResource> institutions) {
        InstitutionsResource institutionsResource = null;
        if (institutions != null) {
            institutionsResource = new InstitutionsResource();
            institutionsResource.setItems(institutions);
            institutionsResource.setTotalCount(institutions.size());
        }
        return institutionsResource;
    }

}
