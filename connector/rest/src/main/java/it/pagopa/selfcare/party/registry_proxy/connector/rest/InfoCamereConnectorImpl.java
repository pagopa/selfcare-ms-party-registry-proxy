package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.api.InfoCamereConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereCfRequest;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamerePec;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamerePolling;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.InfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.ClientCredentialsResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.infocamere.InfoCamerePecResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.infocamere.InfoCamerePollingResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.IniPecJwsGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InfoCamereConnectorImpl implements InfoCamereConnector {
    private final InfoCamereRestClient restClient;
    private final IniPecJwsGenerator iniPecJwsGenerator;

    public InfoCamereConnectorImpl(InfoCamereRestClient restClient, IniPecJwsGenerator iniPecJwsGenerator) {
        log.trace("Initializing {}", InfoCamereConnectorImpl.class.getSimpleName());
        this.restClient = restClient;
        this.iniPecJwsGenerator = iniPecJwsGenerator;
    }

    @Override
    public Businesses businessesByLegalTaxId(String legalTaxId) {
        log.trace("start businessesByLegal");
        String accessToken = "Bearer " + this.getToken().getAccessToken();
        return this.restClient.businessesByLegalTaxId(legalTaxId, accessToken);
    }

    private ClientCredentialsResponse getToken() {
        log.trace("start getToken");
        String jws = "Bearer " + iniPecJwsGenerator.createAuthRest();
        return this.restClient.getToken(jws);
    }

    @Override
    public InfoCamerePolling callEServiceRequestId(InfoCamereCfRequest infoCamereCfRequest) {
        log.trace("start callEServiceRequestId with cf size: {}",infoCamereCfRequest.getElencoCf().size());
        InfoCamerePollingResponse infoCamerePollingResponse = restClient.callEServiceRequestId(infoCamereCfRequest);
        return convertIniPecPolling(infoCamerePollingResponse);
    }

    @Override
    public InfoCamerePec callEServiceRequestPec(String correlationId) {
        log.trace("start callEServiceRequestPec with correlationId: {}",correlationId);
        InfoCamerePecResponse infoCamerePecResponse = restClient.callEServiceRequestPec(correlationId);
        return convertIniPecPec(infoCamerePecResponse);
    }

    private InfoCamerePolling convertIniPecPolling(InfoCamerePollingResponse response){
        InfoCamerePolling infoCamerePolling = new InfoCamerePolling();
        infoCamerePolling.setDataOraRichiesta(response.getDataOraRichiesta());
        infoCamerePolling.setIdentificativoRichiesta(response.getIdentificativoRichiesta());
        return infoCamerePolling;
    }

    private InfoCamerePec convertIniPecPec(InfoCamerePecResponse response){
        InfoCamerePec iniPec = new InfoCamerePec();
        iniPec.setElencoPec(response.getElencoPec());
        iniPec.setIdentificativoRichiesta(response.getIdentificativoRichiesta());
        iniPec.setDataOraDownload(response.getDataOraDownload());
        return iniPec;
    }
}
