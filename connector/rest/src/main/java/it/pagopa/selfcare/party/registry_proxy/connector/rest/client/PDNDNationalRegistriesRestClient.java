package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.LegalAddressRequest;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.national-registries.serviceCode}", url = "${rest-client.national-registries.base-url}")
public interface PDNDNationalRegistriesRestClient {

    @GetMapping(value = "${rest-client.national-registries.getLegalAddress.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    List<PDNDImpresa> getInstitutionPdndbyDescription (@RequestParam String description);

}
