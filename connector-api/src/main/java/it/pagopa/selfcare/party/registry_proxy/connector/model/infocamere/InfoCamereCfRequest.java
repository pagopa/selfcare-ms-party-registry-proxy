package it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere;

import lombok.Data;

import java.util.List;

@Data
public class InfoCamereCfRequest {
    private String dataOraRichiesta;
    private List<String> elencoCf;
}
