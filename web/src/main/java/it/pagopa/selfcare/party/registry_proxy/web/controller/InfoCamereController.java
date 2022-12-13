package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Businesses;
import it.pagopa.selfcare.party.registry_proxy.core.InfoCamereService;
import it.pagopa.selfcare.party.registry_proxy.web.model.BusinessesResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.GetBusinessesByLegalDto;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.BusinessesMapper;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.GetBusinessesByLegalMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/v1")
@Api(tags = "infocamere")
public class InfoCamereController {

    private final InfoCamereService infoCamereService;

    @Autowired
    public InfoCamereController(InfoCamereService infoCamereService) {
        log.trace("Initializing {}", InfoCamereController.class.getSimpleName());
        this.infoCamereService = infoCamereService;
    }

    @PostMapping("/businesses")
    public ResponseEntity<BusinessesResource> businessesByLegal(GetBusinessesByLegalDto getBusinessesByLegalDto) {
        Businesses businesses = infoCamereService.businessesByLegal(GetBusinessesByLegalMapper.fromDto(getBusinessesByLegalDto));
        return ResponseEntity.ok().body(BusinessesMapper.toResource(businesses));
    }
}
