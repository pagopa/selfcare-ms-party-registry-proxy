package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereLocationAddress;
import it.pagopa.selfcare.party.registry_proxy.web.model.LocationAddress;

public class LocationAddressMapper {
    public static LocationAddress toResource(InfoCamereLocationAddress infoCamereLocationAddress) {
        LocationAddress locationAddress = null;
        if(infoCamereLocationAddress != null) {
            locationAddress = new LocationAddress();
            locationAddress.setAddress(createLegalAddress(infoCamereLocationAddress));
            locationAddress.setZip(infoCamereLocationAddress.getPostalCode());
            locationAddress.setProvince(infoCamereLocationAddress.getProvince());
            locationAddress.setMunicipality(infoCamereLocationAddress.getMunicipality());
            locationAddress.setDescription(infoCamereLocationAddress.getAddress());
        }
        return locationAddress;
    }

    private static String createLegalAddress(InfoCamereLocationAddress address) {
        return address.getToponym() + " " + address.getStreet() + " " + address.getStreetNumber();
    }
}
