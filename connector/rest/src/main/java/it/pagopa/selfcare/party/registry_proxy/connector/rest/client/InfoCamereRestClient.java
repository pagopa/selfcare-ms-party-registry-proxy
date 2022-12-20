package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereCfRequest;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.ClientCredentialsResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.infocamere.InfoCamerePecResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.infocamere.InfoCamerePollingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.info-camere.serviceCode}", url = "${rest-client.info-camere.base-url}")
public interface InfoCamereRestClient {
    @GetMapping(value = "${rest-client.info-camere.token.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    ClientCredentialsResponse getToken(@RequestHeader("Authorization") String bearerToken);

    @GetMapping(value = "${rest-client.info-camere.businessesByLegal.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    Businesses businessesByLegalTaxId(@PathVariable("taxId") String taxId, @RequestHeader("Authorization") String accessToken);

    @PostMapping(value = "${rest-client.info-camere.callEServiceRequestId.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    InfoCamerePollingResponse callEServiceRequestId(@RequestBody InfoCamereCfRequest infoCamereCfRequest);

    @PostMapping(value = "${rest-client.info-camere.callEServiceRequestPec.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    InfoCamerePecResponse callEServiceRequestPec(@PathVariable("correlationId") String correlationId);

}
