package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.InvalidRequestException;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.core.PDNDInfoCamereService;
import it.pagopa.selfcare.party.registry_proxy.web.model.PDNDBusinessResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.PDNDInfoCamereBusinessMapper;

import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/visura-infocamere-pdnd", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "visura-infocamere-pdnd")
public class VisuraInfoCamereController {

  private final PDNDInfoCamereService pdndInfoCamereService;
  private final PDNDInfoCamereBusinessMapper pdndBusinessMapper;

  public VisuraInfoCamereController(
          PDNDInfoCamereService pdndInfoCamereService,
          PDNDInfoCamereBusinessMapper pdndBusinessMapper) {
    this.pdndInfoCamereService = pdndInfoCamereService;
    this.pdndBusinessMapper = pdndBusinessMapper;
  }

  @ResponseStatus(HttpStatus.OK)
  @Operation(
          summary = "${swagger.api.infocamere-pdnd.institution.summary}",
          description = "${swagger.api.infocamere-pdnd.institution.notes}",
          operationId = "institutionVisuraPdndByTaxCodeUsingGET")
  @GetMapping("/institutions/{taxCode}")
  public ResponseEntity<PDNDBusinessResource> getInstitution(@ApiParam("${swagger.model.institution.taxCode}") @PathVariable String taxCode) {
    PDNDBusiness business = pdndInfoCamereService.retrieveInstitutionDetail(taxCode);
    return ResponseEntity.ok().body(pdndBusinessMapper.toResource(business));
  }

  @ResponseStatus(HttpStatus.OK)
  @Operation(
          summary = "${swagger.api.infocamere-pdnd.institutions.summary}",
          description = "${swagger.api.infocamere-pdnd.institutions.notes}",
          operationId = "institutionsPdndByReaGET")
  @GetMapping("/institutions")
  public ResponseEntity<PDNDBusinessResource> institutionsPdndByRea(
          @ApiParam("${swagger.model.institution.description}") @RequestParam String rea,
          @ApiParam("${swagger.model.institution.description}") @RequestParam String county) {
    PDNDBusiness business = pdndInfoCamereService.retrieveInstitutionFromRea(rea, county);
    return ResponseEntity.ok().body(pdndBusinessMapper.toResource(business));
  }
}
