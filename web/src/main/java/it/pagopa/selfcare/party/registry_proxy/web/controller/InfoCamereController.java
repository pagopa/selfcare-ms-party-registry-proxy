package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.Businesses;
import it.pagopa.selfcare.party.registry_proxy.core.NationalRegistriesService;
import it.pagopa.selfcare.party.registry_proxy.web.model.BusinessesResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.GetInstitutionsByLegalDto;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.BusinessesMapper;
import it.pagopa.selfcare.party.registry_proxy.web.utils.RequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/info-camere", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "infocamere")
public class InfoCamereController {

    private final NationalRegistriesService nationalRegistriesService;

    @Autowired
    public InfoCamereController(NationalRegistriesService nationalRegistriesService) {
        log.trace("Initializing {}", InfoCamereController.class.getSimpleName());
        this.nationalRegistriesService = nationalRegistriesService;
    }
    @ResponseStatus(HttpStatus.OK)
    @Tag(name = "support-pnpg")
    @Tag(name = "infocamere")
    @ApiOperation(value = "${swagger.api.info-camere.institutions.summary}",
            notes = "${swagger.api.info-camere.institutions.notes}")
    @PostMapping("/institutions")
    public ResponseEntity<BusinessesResource> institutionsByLegalTaxId(@RequestBody GetInstitutionsByLegalDto getInstitutionsByLegalDto) {
        RequestValidator.validateTaxId(getInstitutionsByLegalDto.getFilter().getLegalTaxId());
        Businesses businesses = nationalRegistriesService.institutionsByLegalTaxId(getInstitutionsByLegalDto.getFilter().getLegalTaxId());
        return ResponseEntity.ok().body(BusinessesMapper.toResource(businesses));
    }
}
