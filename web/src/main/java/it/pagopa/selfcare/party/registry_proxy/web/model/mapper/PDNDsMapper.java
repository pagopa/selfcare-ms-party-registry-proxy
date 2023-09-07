package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.web.model.PDNDResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.PDNDsResource;

import java.util.List;

public class PDNDsMapper {

    public static PDNDsResource toResource(List<PDNDResource> resources, long count) {
        PDNDsResource pdndResource = null;
        if (resources != null) {
            pdndResource = new PDNDsResource();
            pdndResource.setItems(resources);
            pdndResource.setCount(count);
        }
        return pdndResource;
    }

}
