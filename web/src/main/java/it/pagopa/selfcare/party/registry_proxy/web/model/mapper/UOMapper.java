package it.pagopa.selfcare.party.registry_proxy.web.model.mapper;

import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;
import it.pagopa.selfcare.party.registry_proxy.web.model.UOResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.UOsResource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
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

            uoResource.setDataIstituzione(uo.getDataIstituzione());
            uoResource.setNomeResponsabile(uo.getNomeResponsabile());
            uoResource.setCognomeResponsabile(uo.getCognomeResponsabile());
            uoResource.setMailResponsabile(uo.getMailResponsabile());
            uoResource.setTelefonoResponsabile(uo.getTelefonoResponsabile());
            uoResource.setCodiceComuneISTAT(uo.getCodiceComuneISTAT());
            uoResource.setCodiceCatastaleComune(uo.getCodiceCatastaleComune());
            uoResource.setCAP(uo.getCAP());
            uoResource.setIndirizzo(uo.getIndirizzo());
            uoResource.setTelefono(uo.getTelefono());
            uoResource.setFax(uo.getFax());
            uoResource.setTipoMail1(uo.getTipoMail1());
            uoResource.setUrl(uo.getUrl());
            uoResource.setDataAggiornamento(uo.getDataAggiornamento());
        }
        return uoResource;
    }

    public static UOsResource toResource(List<UOResource> uoList, long count) {
        UOsResource uosResource = null;
        if (uoList != null) {
            uosResource = new UOsResource();
            uosResource.setItems(uoList);
            uosResource.setCount(count);
        }
        return uosResource;
    }

}
