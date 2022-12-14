package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;


import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.web.model.BusinessesResource;

import java.util.stream.Collectors;

public class BusinessesMapper {
    public static BusinessesResource toResource(Businesses businesses) {
        BusinessesResource resource = null;
        if(businesses != null) {
            resource = new BusinessesResource();
            resource.setLegalTaxId(businesses.getLegalTaxId());
            resource.setRequestDateTime(businesses.getRequestDateTime());
            if(businesses.getBusinesses() != null) {
                resource.setBusinesses(
                        businesses.getBusinesses()
                                .stream()
                                .map(BusinessMapper::toResource)
                                .collect(Collectors.toList())
                );
            }

        }

        return resource;
    }
}
