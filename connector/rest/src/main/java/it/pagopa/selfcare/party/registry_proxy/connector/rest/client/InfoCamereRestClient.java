package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.info-camere.serviceCode}", url = "${rest-client.info-camere.base-url}")
public interface InfoCamereRestClient {

    @GetMapping(value = "${rest-client.info-camere.institutionsByLegal.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    Businesses institutionsByLegalTaxId(@PathVariable("taxId") String taxId, @RequestHeader("Authorization") String accessToken, @RequestParam(name = "client_id") String clientId);

}
