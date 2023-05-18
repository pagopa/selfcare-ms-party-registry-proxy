package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.web.model.UOResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.UOsResource;

import java.util.List;

public class UOsMapper {

    public static UOsResource toResource(List<UOResource> UOs, long count) {
        UOsResource uosResource = null;
        if (UOs != null) {
            uosResource = new UOsResource();
            uosResource.setItems(UOs);
            uosResource.setCount(count);
        }
        return uosResource;
    }

}
