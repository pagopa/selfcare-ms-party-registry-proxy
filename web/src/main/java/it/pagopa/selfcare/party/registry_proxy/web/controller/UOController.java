package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.model.UO;
import it.pagopa.selfcare.party.registry_proxy.core.UOService;
import it.pagopa.selfcare.party.registry_proxy.web.model.UOResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.UOsResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.UOMapper;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.UOsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "uo")
public class UOController {

    private final UOService uoService;


    @Autowired
    public UOController(UOService uoService) {
        log.trace("Initializing {}", UOController.class.getSimpleName());
        this.uoService = uoService;
    }


    @GetMapping("uos")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.uo.findUOs.summary}",
            notes = "${swagger.api.uo.findUOs.notes}")
    public UOsResource findUOs(@ApiParam("${swagger.model.*.origin}")
                                             @RequestParam(value = "origin", required = false) Optional<Origin> origin,
                               @ApiParam(value = "${swagger.model.*.page}")
                                             @RequestParam(value = "page", required = false, defaultValue = "1")
                                                     Integer page,
                               @ApiParam(value = "${swagger.model.*.limit}")
                                             @RequestParam(value = "limit", required = false, defaultValue = "10")
                                                     Integer limit) {
        log.trace("findUOs start");
        log.debug("findUOs origin = {}, page = {}, limit = {}", origin, page, limit);
        final QueryResult<UO> result = uoService.search(origin, page, limit);
        final UOsResource uosResource = UOsMapper.toResource(result.getItems().stream()
                .map(UOMapper::toResource)
                .collect(Collectors.toList()),
                result.getTotalHits());
        log.debug("findUOs result = {}", uosResource);
        log.trace("findUOs end");
        return uosResource;
    }


    @GetMapping("origins/{origin}/uos/{codiceUniUo}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.uo.findUO.summary}",
            notes = "${swagger.api.uo.findUO.notes}")
    public UOResource findUO(@ApiParam("${swagger.model.*.origin}")
                                         @PathVariable("origin") Origin origin,
                              @ApiParam("${swagger.mode.uo.codiceUniUo}")
                                         @PathVariable("codiceUniUo") String codiceUniUo) {
        log.trace("findUO start");
        log.debug("findUO origin = {}, codiceUniUo = {}", origin, codiceUniUo);
        final UOResource uoResource = UOMapper.toResource(uoService.findById(codiceUniUo, origin));
        log.debug("findUO result = {}", uoResource);
        log.trace("findUO end");
        return uoResource;
    }

}
