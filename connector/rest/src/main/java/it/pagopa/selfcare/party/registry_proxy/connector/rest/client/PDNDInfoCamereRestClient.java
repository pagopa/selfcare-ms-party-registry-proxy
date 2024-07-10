package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.pdnd-infocamere.serviceCode}", url = "${rest-client.pdnd-infocamere.base-url}")
public interface PDNDInfoCamereRestClient {

    @GetMapping(value = "${rest-client.pdnd-infocamere.getDescription.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    List<PDNDImpresa> retrieveInstitutionsPdndByDescription(@RequestParam String description);

    @GetMapping(value = "${rest-client.pdnd-infocamere.getTaxCode.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    List<PDNDImpresa> retrieveInstitutionPdndByTaxCode(@RequestParam String taxCode);

}
