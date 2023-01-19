package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereLegalAddress;
import it.pagopa.selfcare.party.registry_proxy.core.InfoCamereBatchRequestService;
import it.pagopa.selfcare.party.registry_proxy.core.InfoCamereService;
import it.pagopa.selfcare.party.registry_proxy.web.model.*;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.BusinessesMapper;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.GetDigitalAddressInfoCamereMapper;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.LegalAddressMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/v1/info-camere", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "infocamere")
public class InfoCamereController {

    private final InfoCamereService infoCamereService;
    private final InfoCamereBatchRequestService infoCamereBatchRequestService;
    @Autowired
    public InfoCamereController(InfoCamereService infoCamereService,
                                InfoCamereBatchRequestService infoCamereBatchRequestService) {
        log.trace("Initializing {}", InfoCamereController.class.getSimpleName());
        this.infoCamereService = infoCamereService;
        this.infoCamereBatchRequestService = infoCamereBatchRequestService;
    }
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.info-camere.institutions.summary}",
            notes = "${swagger.api.info-camere.institutions.notes}")
    @PostMapping("/institutions")
    public ResponseEntity<BusinessesResource> institutionsByLegalTaxId(@RequestBody GetInstitutionsByLegalDto getInstitutionsByLegalDto) {
        Businesses businesses = infoCamereService.institutionsByLegalTaxId(getInstitutionsByLegalDto.getFilter().getLegalTaxId());
        return ResponseEntity.ok().body(BusinessesMapper.toResource(businesses));
    }

    @PostMapping("/digital-address")
    public ResponseEntity<GetDigitalAddressInfoCamereOKDto> createBatchRequest(@RequestBody @Valid GetDigitalAddressInfoCamereRequestBodyDto dto) {
        String cf = dto.getFilter().getTaxId();
        InfoCamereBatchRequest infoCamereBatchRequest = infoCamereService.createBatchRequestByCf(cf);
        return ResponseEntity.ok(GetDigitalAddressInfoCamereMapper.toResource(infoCamereBatchRequest));
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody @Valid GetDigitalAddressInfoCamereRequestBodyDto dto) {
        infoCamereBatchRequestService.batchPecListRequest();
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/legal-address")
    public ResponseEntity<LegalAddressResource> legalAddressByTaxId(@RequestBody ProfessionalAddressRequestBodyDto getAddressRegistroImpreseRequestBodyDto) {
        InfoCamereLegalAddress infoCamereLegalAddressResponse = infoCamereService.legalAddressByTaxId(getAddressRegistroImpreseRequestBodyDto.getFilter().getTaxId());
        return ResponseEntity.ok().body(LegalAddressMapper.toResource(infoCamereLegalAddressResponse));
    }
}
