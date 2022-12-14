package it.pagopa.selfcare.party.registry_proxy.connector.rest.model.infocamere;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Pec;
import lombok.Data;

import java.util.List;

@Data
public class InfoCamerePecResponse {
    private String dataOraDownload;
    private String identificativoRichiesta;
    private List<Pec> elencoPec;
}
