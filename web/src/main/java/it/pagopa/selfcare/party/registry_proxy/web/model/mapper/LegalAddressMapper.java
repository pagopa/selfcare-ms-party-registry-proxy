package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereLegalAddress;
import it.pagopa.selfcare.party.registry_proxy.web.model.LegalAddressResource;

public class LegalAddressMapper {
    public static LegalAddressResource toResource(InfoCamereLegalAddress infoCamereLegalAddress) {
        LegalAddressResource resource = null;
        if(infoCamereLegalAddress != null) {
            resource = new LegalAddressResource();
            resource.setTaxId(infoCamereLegalAddress.getTaxId());
            resource.setDateTimeExtraction(infoCamereLegalAddress.getDateTimeExtraction());
            resource.setLegalAddress(LocationAddressMapper.toResource(infoCamereLegalAddress.getLegalAddress()));
        }
        return resource;
    }
}
