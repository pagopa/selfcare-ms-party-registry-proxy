package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereCfRequest;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamerePec;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Pec;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.InfoCamereRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.TokenRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.ClientCredentialsResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.TokenType;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.infocamere.InfoCamerePecResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.infocamere.InfoCamerePollingResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.IniPecJwsGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class InfoCamereConnectorTest {
    @InjectMocks
    private InfoCamereConnectorImpl infoCamereClientImpl;

    @Mock
    private InfoCamereRestClient infoCamereRestClient;

    @Mock
    private IniPecJwsGenerator iniPecJwsGenerator;

    @Mock
    private TokenRestClient tokenRestClient;

    @Test
    void testCallEServiceRequestId() {
        InfoCamerePollingResponse infoCamerePollingResponse = new InfoCamerePollingResponse();
        infoCamerePollingResponse.setDataOraRichiesta("Data Ora Richiesta");
        infoCamerePollingResponse.setIdentificativoRichiesta("Identificativo Richiesta");
        String jws = "jws";
        when(iniPecJwsGenerator.createAuthRest(any())).thenReturn(jws);
        when(tokenRestClient.getToken(any())).thenReturn("ABC123");
        when(infoCamereRestClient.callEServiceRequestId(any(),any())).thenReturn(infoCamerePollingResponse);

        InfoCamereCfRequest infoCamereCfRequest = new InfoCamereCfRequest();
        infoCamereCfRequest.setDataOraRichiesta("Data Ora Richiesta");
        infoCamereCfRequest.setElencoCf(new ArrayList<>());
        assertEquals("Data Ora Richiesta", infoCamereCfRequest.getDataOraRichiesta());
        assertEquals(0, infoCamereCfRequest.getElencoCf().size());
        assertNotNull(infoCamereClientImpl.callEServiceRequestId(infoCamereCfRequest));
    }

    @Test
    void testCallEServiceRequestPec() {
        InfoCamerePecResponse infoCamerePecResponse = new InfoCamerePecResponse();
        Pec pec = new Pec();
        pec.setPecImpresa("pecImpresa");
        pec.setCf("cf");
        pec.setPecProfessionistas(new ArrayList<>());
        List<Pec> pecs = new ArrayList<>();
        pecs.add(pec);
        infoCamerePecResponse.setElencoPec(pecs);
        infoCamerePecResponse.setIdentificativoRichiesta("correlationId");
        infoCamerePecResponse.setDataOraDownload("Data Ora Download");
        String jws = "jws";
        when(iniPecJwsGenerator.createAuthRest(any())).thenReturn(jws);
        when(tokenRestClient.getToken(any())).thenReturn("ABC123");
        when(infoCamereRestClient.callEServiceRequestPec(any(),any())).thenReturn(infoCamerePecResponse);

        InfoCamerePec infoCamerePec = infoCamereClientImpl.callEServiceRequestPec("correlationId");
        assertEquals(infoCamerePec.getElencoPec().get(0).getPecImpresa(),"pecImpresa");
        assertEquals(infoCamerePec.getElencoPec().get(0).getCf(),"cf");
        assertEquals(infoCamerePec.getElencoPec().get(0).getPecProfessionistas().size(),0);

        assertEquals("correlationId", infoCamerePec.getIdentificativoRichiesta());
        assertEquals(1, infoCamerePec.getElencoPec().size());
        assertEquals(infoCamerePec.getDataOraDownload(),"Data Ora Download");
    }
}

