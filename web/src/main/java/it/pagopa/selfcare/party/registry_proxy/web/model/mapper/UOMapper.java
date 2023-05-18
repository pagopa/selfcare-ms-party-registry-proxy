package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;
import it.pagopa.selfcare.party.registry_proxy.web.model.UOResource;

public class UOMapper {

    public static UOResource toResource(UO uo) {
        UOResource uoResource = null;
        if (uo != null) {
            uoResource = new UOResource();
            uoResource.setId(uo.getId());
            uoResource.setOrigin(uo.getOrigin());
            uoResource.setCodiceIpa(uo.getCodiceIpa());
            uoResource.setCodiceUniAoo(uo.getCodiceUniAoo());
            uoResource.setCodiceUniUo(uo.getCodiceUniUo());
            uoResource.setCodiceUniUoPadre(uo.getCodiceUniUoPadre());
            uoResource.setCodiceFiscaleEnte(uo.getCodiceFiscaleEnte());
            uoResource.setDenominazioneEnte(uo.getDenominazioneEnte());
            uoResource.setDescrizioneUo(uo.getDescrizioneUo());
            uoResource.setMail1(uo.getMail1());
        }
        return uoResource;
    }

}
