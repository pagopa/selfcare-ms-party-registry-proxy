package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.core.PDNDNationalRegistriesService;
import it.pagopa.selfcare.party.registry_proxy.web.model.PDNDBusinessResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.PDNDInfoCamereBusinessMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/infocamere-pdnd", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "infocamere PDND")
public class PDNDInfoCamereController {

    private final PDNDNationalRegistriesService nationalRegistriesPdndService;
    private final PDNDInfoCamereBusinessMapper pdndBusinessMapper;

    public PDNDInfoCamereController(PDNDNationalRegistriesService nationalRegistriesPdndService, PDNDInfoCamereBusinessMapper pdndBusinessMapper) {
        this.nationalRegistriesPdndService = nationalRegistriesPdndService;
        this.pdndBusinessMapper = pdndBusinessMapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.info-camere-pdnd.institutions.summary}",
            notes = "${swagger.api.info-camere-pdnd.institutions.notes}")
    @GetMapping("/institutions")
    public ResponseEntity<List<PDNDBusinessResource>> institutionsPdndByDescription(@ApiParam("${swagger.model.institution.description}")
                                                                                        @RequestParam String description) {
        List<PDNDBusiness> businessList = nationalRegistriesPdndService.retrieveInstitutionsPdndByDescription(description);
        return ResponseEntity.ok().body(pdndBusinessMapper.toResources(businessList));
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.info-camere-pdnd.institutions.summary}",
            notes = "${swagger.api.info-camere-pdnd.institutions.notes}")
    @GetMapping("/institution/{taxCode}")
    public ResponseEntity<PDNDBusinessResource> institutionPdndByTaxCode(@ApiParam("${swagger.model.institution.taxCode}")
                                                                                    @PathVariable String taxCode) {
        PDNDBusiness business = nationalRegistriesPdndService.retrieveInstitutionPdndByTaxCode(taxCode);
        return ResponseEntity.ok().body(pdndBusinessMapper.toResource(business));
    }
}
