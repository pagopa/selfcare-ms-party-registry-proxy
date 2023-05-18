package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.web.model.AOOResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.AOOsResource;

import java.util.List;

public class AOOsMapper {

    public static AOOsResource toResource(List<AOOResource> aoos, long count) {
        AOOsResource aoosResource = null;
        if (aoos != null) {
            aoosResource = new AOOsResource();
            aoosResource.setItems(aoos);
            aoosResource.setCount(count);
        }
        return aoosResource;
    }

}
