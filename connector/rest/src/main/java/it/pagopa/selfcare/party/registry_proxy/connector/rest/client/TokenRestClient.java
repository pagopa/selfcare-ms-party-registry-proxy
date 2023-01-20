package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.ClientCredentialsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@FeignClient(name = "${rest-client.info-camere.token.serviceCode}", url = "${rest-client.info-camere.token.url}")
public interface TokenRestClient {
    @PostMapping(consumes = TEXT_PLAIN_VALUE)
    @ResponseBody
    String getToken(@RequestBody String bearerToken);
}
