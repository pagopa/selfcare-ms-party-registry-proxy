package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;


import it.pagopa.selfcare.party.registry_proxy.connector.model.GetBusinessesByLegal;
import it.pagopa.selfcare.party.registry_proxy.web.model.GetBusinessesByLegalDto;

public class GetBusinessesByLegalMapper {
    public static GetBusinessesByLegal fromDto(GetBusinessesByLegalDto getBusinessesByLegalDto) {
        GetBusinessesByLegal getBusinessesByLegal = new GetBusinessesByLegal();
        getBusinessesByLegal.setLegalTaxId(getBusinessesByLegalDto.getLegalTaxId());
        return getBusinessesByLegal;
    }
}
