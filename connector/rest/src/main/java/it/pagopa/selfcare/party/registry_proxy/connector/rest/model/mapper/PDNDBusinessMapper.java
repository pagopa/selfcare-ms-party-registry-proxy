package it.pagopa.selfcare.party.registry_proxy.connector.rest.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.national_registries_pdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.visura.ClassificazioneAteco;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.visura.ClassificazioniAteco;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.visura.Localizzazione;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PDNDImpresa;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.PDNDVisuraImpresa;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Mapping(target = "vatNumber", source = "datiIdentificativiImpresa.vatNumber")
    @Mapping(target = "legalForm", source = "datiIdentificativiImpresa.legalForm")
    @Mapping(target = "atecoCodes", source = "infoAttivita.classificazioniAteco", qualifiedByName = "mapAtecoCodes")
    @Mapping(target = "address", source = "datiIdentificativiImpresa.localizzazione", qualifiedByName = "mapAddress")
    @Mapping(target = "disabledStateInstitution", source = "infoAttivita.disabledStateInstitution")
    @Mapping(target = "descriptionStateInstitution", source = "infoAttivita.descriptionStateInstitution")
    @Mapping(target = "statusCompanyRI", source = "datiIdentificativiImpresa.statusCompanyRI")
    @Mapping(target = "statusCompanyRD", source = "datiIdentificativiImpresa.statusCompanyRD")
    PDNDBusiness toPDNDBusiness(PDNDVisuraImpresa pdndImpresa);

    @Named("mapAtecoCodes")
    default List<String> mapAtecoCodes(ClassificazioniAteco classificazioniAteco) {
        if (classificazioniAteco == null || classificazioniAteco.getClassificazioniAteco() == null) {
            return Collections.emptyList();
        }

        return classificazioniAteco.getClassificazioniAteco().stream()
                .map(ClassificazioneAteco::getCodiceAttivita)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    @Named("mapAddress")
    default String mapAddress(Localizzazione localizzazione) {
        return localizzazione.getToponimo() + " " + localizzazione.getVia() + ", " + localizzazione.getCivico();
    }

}
