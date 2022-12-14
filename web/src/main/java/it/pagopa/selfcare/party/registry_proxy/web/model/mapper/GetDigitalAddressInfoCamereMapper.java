package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.party.registry_proxy.web.model.GetDigitalAddressInfoCamereOKDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class GetDigitalAddressInfoCamereMapper {

    public static GetDigitalAddressInfoCamereOKDto toResource(InfoCamereBatchRequest infoCamereBatchRequest) {
        if (infoCamereBatchRequest == null) {
            return null;
        }
        GetDigitalAddressInfoCamereOKDto resource = new GetDigitalAddressInfoCamereOKDto();
        resource.setCorrelationId(infoCamereBatchRequest.getCorrelationId());
        return resource;
    }

}
