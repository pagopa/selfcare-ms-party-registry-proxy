package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;
import it.pagopa.selfcare.party.registry_proxy.core.UOService;
import it.pagopa.selfcare.party.registry_proxy.web.model.UOResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.UOsResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.UOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "uo", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "uo")
public class UOController {

    private final UOService uoService;


    public UOController(UOService uoService) {
        log.trace("Initializing {}", UOController.class.getSimpleName());
        this.uoService = uoService;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.uo.findAll.summary}",
            notes = "${swagger.api.uo.findAll.notes}")
    public UOsResource findAll(@ApiParam(value = "${swagger.model.*.page}")
                               @RequestParam(value = "page", required = false, defaultValue = "1")
                               Integer page,
                               @ApiParam(value = "${swagger.model.*.limit}")
                               @RequestParam(value = "limit", required = false, defaultValue = "10")
                               Integer limit) {
        log.trace("find all UO start");
        log.debug("find all UO, page = {}, limit = {}", page, limit);
        final QueryResult<UO> result = uoService.findAll(page, limit);
        final UOsResource uosResource = UOMapper.toResource(result.getItems().stream()
                        .map(UOMapper::toResource)
                        .collect(Collectors.toList()),
                result.getTotalHits());
        log.debug("find all UO result = {}", uosResource);
        log.trace("find all UO end");
        return uosResource;
    }


    @GetMapping("/{codiceUniAoo}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.uo.findBy.summary}",
            notes = "${swagger.api.uo.findBy.notes}")
    public UOResource findByUnicode(@ApiParam("${swagger.mode.uo.codiceUniUo}")
                                    @PathVariable("codiceUniAoo") String codiceUniUo) {
                                    @PathVariable("codiceUniUo") String codiceUniUo) {
        log.trace("find UO start");
        log.debug("find UO = {}", codiceUniUo);
        final UOResource uoResource = UOMapper.toResource(uoService.findByUnicode(codiceUniUo));
        log.debug("find UO result = {}", uoResource);
        log.trace("find UO end");
        return uoResource;
    }

}
