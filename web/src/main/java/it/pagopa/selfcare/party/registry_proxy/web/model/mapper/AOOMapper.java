package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import it.pagopa.selfcare.party.registry_proxy.web.model.AOOResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.AOOsResource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class AOOMapper {

    public static AOOResource toResource(AOO aoo) {
        AOOResource aooResource = null;
        if (aoo != null) {
            aooResource = new AOOResource();
            aooResource.setId(aoo.getId());
            aooResource.setCodAoo(aoo.getCodAoo());
            aooResource.setCodiceFiscaleEnte(aoo.getCodiceFiscaleEnte());
            aooResource.setCodiceUniAoo(aoo.getCodiceUniAoo());
            aooResource.setCodiceIpa(aoo.getCodiceIpa());
            aooResource.setDenominazioneEnte(aoo.getDenominazioneEnte());
            aooResource.setDenominazioneAoo(aoo.getDenominazioneAoo());
            aooResource.setOrigin(aoo.getOrigin());
            aooResource.setMail1(aoo.getMail1());
        }
        return aooResource;
    }

    public static AOOsResource toResource(List<AOOResource> aooList, long count) {
        AOOsResource aoosResource = null;
        if (aooList != null) {
            aoosResource = new AOOsResource();
            aoosResource.setItems(aooList);
            aoosResource.setCount(count);
        }
        return aoosResource;
    }

}
