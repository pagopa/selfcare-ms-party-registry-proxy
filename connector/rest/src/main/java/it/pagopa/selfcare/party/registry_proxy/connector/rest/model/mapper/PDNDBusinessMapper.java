package it.pagopa.selfcare.party.registry_proxy.connector.rest.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PDNDImpresa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PDNDBusinessMapper {

    @Mapping(source = "getAddress", target = "address")
    List<PDNDBusiness> toPDNDBusinesses(List<PDNDImpresa> pdndImpresaList);

    PDNDBusiness toPDNDBusiness(PDNDImpresa pdndImpresa);

}
