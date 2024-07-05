package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.core.PDNDNationalRegistriesService;
import it.pagopa.selfcare.party.registry_proxy.web.model.PDNDBusinessResponse;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.PDNDBusinessMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/info-camere-pdnd", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "infocamere PDND")
public class InfoCamerePdndController {

    private final PDNDNationalRegistriesService nationalRegistriesPdndService;
    private final PDNDBusinessMapper pdndBusinessMapper;

    public InfoCamerePdndController (PDNDNationalRegistriesService nationalRegistriesPdndService, PDNDBusinessMapper pdndBusinessMapper) {
        this.nationalRegistriesPdndService = nationalRegistriesPdndService;
        this.pdndBusinessMapper = pdndBusinessMapper;
    }


    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.info-camere.institutions.summary}",
            notes = "${swagger.api.info-camere.institutions.notes}")
    @GetMapping("/institutions")
    public ResponseEntity<List<PDNDBusinessResponse>> institutionsPdndByDescription(@RequestParam String description) {
        List<PDNDBusiness> businessList = nationalRegistriesPdndService.institutionsPdndByDescription(description);
        return ResponseEntity.ok().body(pdndBusinessMapper.toResponses(businessList));
    }
}
