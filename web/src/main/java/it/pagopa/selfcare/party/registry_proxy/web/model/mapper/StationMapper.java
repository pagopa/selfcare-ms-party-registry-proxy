package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import it.pagopa.selfcare.party.registry_proxy.web.model.PDNDResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StationMapper {
    PDNDResource toResource(Station pdnd);

}
