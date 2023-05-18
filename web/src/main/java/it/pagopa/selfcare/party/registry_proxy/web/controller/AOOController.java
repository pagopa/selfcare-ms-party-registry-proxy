package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AOO;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.core.AOOService;
import it.pagopa.selfcare.party.registry_proxy.web.model.AOOResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.AOOsResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.AOOMapper;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.AOOsMapper;
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
@Api(tags = "aoo")
public class AOOController {

    private final AOOService aooService;

    @Autowired
    public AOOController(AOOService aooService) {
        log.trace("Initializing {}", AOOController.class.getSimpleName());
        this.aooService = aooService;
    }


    @GetMapping("aoos")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.aoo.findAOOs.summary}",
            notes = "${swagger.api.aoo.findAOOs.notes}")
    public AOOsResource findAOOs(@ApiParam("${swagger.model.*.origin}")
                                             @RequestParam(value = "origin", required = false) Optional<Origin> origin,
                                             @ApiParam(value = "${swagger.model.*.page}")
                                             @RequestParam(value = "page", required = false, defaultValue = "1")
                                                     Integer page,
                                             @ApiParam(value = "${swagger.model.*.limit}")
                                             @RequestParam(value = "limit", required = false, defaultValue = "10")
                                                     Integer limit) {
        log.trace("findAOOs start");
        log.debug("findAOOs origin = {}, page = {}, limit = {}", origin, page, limit);
        final QueryResult<AOO> result = aooService.search(origin, page, limit);
        final AOOsResource aoosResource = AOOsMapper.toResource(result.getItems().stream()
                .map(AOOMapper::toResource)
                .collect(Collectors.toList()),
                result.getTotalHits());
        log.debug("findAOOs result = {}", aoosResource);
        log.trace("findAOOs end");
        return aoosResource;
    }


    @GetMapping("origins/{origin}/aoos/{codiceUniAoo}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.category.findAOO.summary}",
            notes = "${swagger.api.category.findAOO.notes}")
    public AOOResource findAOO(@ApiParam("${swagger.model.*.origin}")
                                         @PathVariable("origin") Origin origin,
                                    @ApiParam("${swagger.model.aoo.codiceUniAoo}")
                                         @PathVariable("codiceUniAoo") String codiceUniAoo) {
        log.trace("findAOO start");
        log.debug("findAOO origin = {}, codiceUniAoo = {}", origin, codiceUniAoo);
        final AOOResource aooResource = AOOMapper.toResource(aooService.findById(codiceUniAoo, origin));
        log.debug("findAOO result = {}", aooResource);
        log.trace("findAOO end");
        return aooResource;
    }

}
