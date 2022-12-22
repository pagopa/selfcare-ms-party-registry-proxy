package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.ClientCredentialsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.info-camere.token.serviceCode}", url = "${rest-client.info-camere.token.base-url}")
public interface TokenRestClient {
    @GetMapping(value = "${rest-client.info-camere.token.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    ClientCredentialsResponse getToken(@RequestHeader("Authorization") String bearerToken);
}
