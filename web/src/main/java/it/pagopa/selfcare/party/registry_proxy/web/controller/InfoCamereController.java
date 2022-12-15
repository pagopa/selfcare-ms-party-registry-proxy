package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.connector.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.party.registry_proxy.core.InfoCamereService;
import it.pagopa.selfcare.party.registry_proxy.web.model.BusinessesResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.GetBusinessesByLegalDto;
import it.pagopa.selfcare.party.registry_proxy.web.model.GetDigitalAddressInfoCamereOKDto;
import it.pagopa.selfcare.party.registry_proxy.web.model.GetDigitalAddressInfoCamereRequestBodyDto;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.BusinessesMapper;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.GetBusinessesByLegalMapper;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.GetDigitalAddressInfoCamereMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "infocamere")
public class InfoCamereController {

    private final InfoCamereService infoCamereService;

    public InfoCamereController(InfoCamereService infoCamereService) {
        log.trace("Initializing {}", InfoCamereController.class.getSimpleName());
        this.infoCamereService = infoCamereService;
    }

    @PostMapping("/businesses")
    public ResponseEntity<BusinessesResource> businessesByLegal(@RequestBody GetBusinessesByLegalDto getBusinessesByLegalDto) {
        Businesses businesses = infoCamereService.businessesByLegal(GetBusinessesByLegalMapper.fromDto(getBusinessesByLegalDto));
        return ResponseEntity.ok().body(BusinessesMapper.toResource(businesses));
    }

    @PostMapping("/digital-address")
    public ResponseEntity<GetDigitalAddressInfoCamereOKDto> createBatchRequest(@RequestBody @Valid GetDigitalAddressInfoCamereRequestBodyDto dto) {
        String cf = dto.getFilter().getTaxId();
        InfoCamereBatchRequest infoCamereBatchRequest = infoCamereService.createBatchRequestByCf(cf);
        return ResponseEntity.ok(GetDigitalAddressInfoCamereMapper.toResource(infoCamereBatchRequest));
    }
}
