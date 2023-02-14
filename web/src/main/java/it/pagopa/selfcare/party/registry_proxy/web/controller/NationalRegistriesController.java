package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.NationalRegistriesProfessionalResponse;
import it.pagopa.selfcare.party.registry_proxy.core.NationalRegistriesService;
import it.pagopa.selfcare.party.registry_proxy.web.model.LegalAddressResponse;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.NationalRegistriesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/national-registries", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "nationalRegistries")
public class NationalRegistriesController {

    private final NationalRegistriesService nationalRegistriesService;

    public NationalRegistriesController(NationalRegistriesService nationalRegistriesService) {
        this.nationalRegistriesService = nationalRegistriesService;
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.national-registries.summary}",
            notes = "${swagger.api.national-registries.legal-address}")
    @GetMapping("/legal-address")
    public ResponseEntity<LegalAddressResponse> legalAddress(@RequestParam(value = "taxId") String taxId) {
        NationalRegistriesProfessionalResponse response = nationalRegistriesService.getLegalAddress(taxId);
        return ResponseEntity.ok().body(NationalRegistriesMapper.toResource(response));
    }
}
