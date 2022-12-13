package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GetBusinessesByLegal;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.InfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.ClientCredentialsResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.IniPecJwsGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InfoCamereConnectorImpl implements InfoCamereConnector {
    private InfoCamereRestClient restClient;
    private IniPecJwsGenerator iniPecJwsGenerator;

    @Autowired
    public InfoCamereConnectorImpl(InfoCamereRestClient restClient, IniPecJwsGenerator iniPecJwsGenerator) {
        log.trace("Initializing {}", InfoCamereConnectorImpl.class.getSimpleName());
        this.restClient = restClient;
        this.iniPecJwsGenerator = iniPecJwsGenerator;
    }

    @Override
    public Businesses businessesByLegal(GetBusinessesByLegal getBusinessesByLegal) {
        log.trace("start businessesByLegal");
        String accessToken = "Bearer " + this.getToken().getAccessToken();
        return this.restClient.businessesByLegal(getBusinessesByLegal.getLegalTaxId(), accessToken);
    }

    private ClientCredentialsResponse getToken() {
        log.trace("start getToken");
        String jws = "Bearer " + iniPecJwsGenerator.createAuthRest();
        return this.restClient.getToken(jws);
    }
}
