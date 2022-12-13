package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.ClientCredentialsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.info-camere.serviceCode}", url = "${rest-client.info-camere.base-url}")
public interface InfoCamereRestClient {
    @GetMapping(value = "${rest-client.info-camere.token.path", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    ClientCredentialsResponse getToken(@RequestHeader("Authorization") String bearerToken);

    @GetMapping(value = "${rest-client.info-camere.businessesByLegal.path", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    Businesses businessesByLegal(@PathVariable("taxId") String taxId, @RequestHeader("Authorization") String accessToken);
}
