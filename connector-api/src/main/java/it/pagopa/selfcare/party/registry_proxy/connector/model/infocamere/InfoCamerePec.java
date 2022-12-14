package it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere;

import lombok.Data;

import java.util.List;

@Data
public class InfoCamerePec {
    private String dataOraDownload;
    private String identificativoRichiesta;
    private List<Pec> elencoPec;
}
