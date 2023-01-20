package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.*;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.InfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.TokenRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.InipecScopeEnum;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.IniPecJwsGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InfoCamereConnectorImpl implements InfoCamereConnector {
    private final InfoCamereRestClient restClient;
    private final TokenRestClient tokenRestClient;
    private final IniPecJwsGenerator iniPecJwsGenerator;


    public InfoCamereConnectorImpl(InfoCamereRestClient restClient, TokenRestClient tokenRestClient, IniPecJwsGenerator iniPecJwsGenerator) {
        log.trace("Initializing {}", InfoCamereConnectorImpl.class.getSimpleName());
        this.restClient = restClient;
        this.tokenRestClient = tokenRestClient;
        this.iniPecJwsGenerator = iniPecJwsGenerator;
    }

    @Override
    public Businesses institutionsByLegalTaxId(String legalTaxId) {
        log.info("start institutionsByLegalTaxId");
        String accessToken = "Bearer " + this.getToken(InipecScopeEnum.LEGALE_RAPPRESENTANTE.value());
        return this.restClient.institutionsByLegalTaxId(legalTaxId, accessToken, iniPecJwsGenerator.getClientId());
    }

    private String getToken(String scope) {
        log.trace("start getToken with scope {}", scope);
        String jws = iniPecJwsGenerator.createAuthRest(scope);
        return this.tokenRestClient.getToken(jws);
    }
}
