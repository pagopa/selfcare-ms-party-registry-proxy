package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereCfRequest;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereLegalAddress;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.infocamere.InfoCamerePecResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.infocamere.InfoCamerePollingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.info-camere.serviceCode}", url = "${rest-client.info-camere.base-url}")
public interface InfoCamereRestClient {

    @GetMapping(value = "${rest-client.info-camere.businessesByLegal.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    Businesses businessesByLegalTaxId(@PathVariable("taxId") String taxId, @RequestHeader("Authorization") String accessToken);

    @PostMapping(value = "${rest-client.info-camere.callEServiceRequestId.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    InfoCamerePollingResponse callEServiceRequestId(@RequestBody InfoCamereCfRequest elencoCf, @RequestHeader("Authorization") String accessToken);

    @GetMapping(value = "${rest-client.info-camere.callEServiceRequestPec.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    InfoCamerePecResponse callEServiceRequestPec(@PathVariable("identificativoRichiesta") String identificativoRichiesta, @RequestHeader("Authorization") String accessToken);

    @GetMapping(value = "${rest-client.info-camere.legalAddressByTaxId.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    InfoCamereLegalAddress legalAddressByTaxId(@PathVariable("taxId") String taxId, @RequestHeader("Authorization") String accessToken);
}
