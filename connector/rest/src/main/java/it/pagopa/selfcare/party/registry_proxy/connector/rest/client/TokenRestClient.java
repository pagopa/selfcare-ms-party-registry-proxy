package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@FeignClient(name = "${rest-client.info-camere.token.serviceCode}", url = "${rest-client.info-camere.token.url}")
public interface TokenRestClient {
    @PostMapping(consumes = TEXT_PLAIN_VALUE)
    @ResponseBody
    String getToken(@RequestBody String bearerToken);
}
