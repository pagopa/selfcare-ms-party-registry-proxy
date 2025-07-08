package it.pagopa.selfcare.party.registry_proxy.connector.rest.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.visura.Localizzazione;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PDNDImpresa;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PDNDVisuraImpresa;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PDNDBusinessMapper {

    List<PDNDBusiness> toPDNDBusinesses(List<PDNDImpresa> pdndImpresaList);

    @Mapping(target = "city", source = "businessAddress.city")
    @Mapping(target = "county", source = "businessAddress.county")
    @Mapping(target = "zipCode", source = "businessAddress.zipCode")
    PDNDBusiness toPDNDBusiness(PDNDImpresa pdndImpresa);

    @Mapping(target = ".", source = "datiIdentificativiImpresa")
    @Mapping(target = "city", source = "datiIdentificativiImpresa.localizzazione.comune")
    @Mapping(target = "county", source = "datiIdentificativiImpresa.localizzazione.provincia")
    @Mapping(target = "zipCode", source = "datiIdentificativiImpresa.localizzazione.cap")
    @Mapping(target = "atecoCode", source = "infoAttivita.classificazioniAteco.classificazioneAteco.codiceAttivita")
    @Mapping(target = "address", source = "datiIdentificativiImpresa.localizzazione", qualifiedByName = "mapAddress")
    PDNDBusiness toPDNDBusiness(PDNDVisuraImpresa pdndImpresa);

    @Named("mapAddress")
    default String mapAddress(Localizzazione localizzazione) {
        return localizzazione.getToponimo() + " " + localizzazione.getVia() + ", " + localizzazione.getCivico();
    }

}
