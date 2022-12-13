package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;


import it.pagopa.selfcare.party.registry_proxy.connector.model.Business;
import it.pagopa.selfcare.party.registry_proxy.web.model.BusinessResource;

public class BusinessMapper {
    public static BusinessResource toResource(Business business) {
        BusinessResource resource = new BusinessResource();
        resource.setBusinessName(business.getBusinessName());
        resource.setTaxId(business.getTaxId());
        return resource;
    }
}
