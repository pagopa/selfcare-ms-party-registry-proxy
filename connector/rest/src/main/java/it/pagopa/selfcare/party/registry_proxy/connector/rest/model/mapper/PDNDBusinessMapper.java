package it.pagopa.selfcare.party.registry_proxy.connector.rest.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PDNDImpresa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PDNDBusinessMapper {

    List<PDNDBusiness> toPDNDBusinesses(List<PDNDImpresa> pdndImpresaList);

    @Mapping(target = "city", source = "businessAddress.city")
    @Mapping(target = "county", source = "businessAddress.county")
    @Mapping(target = "zipCode", source = "businessAddress.zipCode")
    PDNDBusiness toPDNDBusiness(PDNDImpresa pdndImpresa);

}
