package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.core.StationService;
import it.pagopa.selfcare.party.registry_proxy.web.model.PDNDResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.PDNDsResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.StationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/station", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "station")
public class StationController {

    private final StationService stationService;
    private final StationMapper stationMapper;

    @Autowired
    public StationController(StationService stationService,
                             StationMapper stationMapper) {
        log.trace("Initializing {}", StationController.class.getSimpleName());
        this.stationService = stationService;
        this.stationMapper = stationMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.station.search.summary}",
            notes = "${swagger.api.station.search.notes}")
    public PDNDsResource search(@ApiParam("${swagger.model.*.search}")
                                @RequestParam(value = "search", required = false)
                                Optional<String> search,
                                @ApiParam(value = "${swagger.model.*.page}")
                                @RequestParam(value = "page", required = false, defaultValue = "1")
                                Integer page,
                                @ApiParam(value = "${swagger.model.*.limit}")
                                @RequestParam(value = "limit", required = false, defaultValue = "10")
                                Integer limit) {
        log.trace("search start");
        log.debug("search search = {}, page = {}, limit = {}", search, page, limit);
        final QueryResult<Station> result = stationService.search(search, page, limit);
        final PDNDsResource pdndResource = PDNDsResource.builder()
                .items(result.getItems().stream()
                        .map(stationMapper::toResource)
                        .collect(Collectors.toList()))
                .count(result.getTotalHits())
                .build();
        log.debug("search result = {}", pdndResource);
        log.trace("search end");
        return pdndResource;
    }

    @GetMapping("/{taxId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.station.search.summary}",
            notes = "${swagger.api.station.search.notes}")
    public PDNDResource searchByTaxCode(@ApiParam("${swagger.model.taxId}")
                                        @PathVariable("taxId") String taxId) {
        log.trace("find SA start");
        log.debug("find SA = {}", taxId);
        final PDNDResource pdndResource = stationMapper.toResource(stationService.findByTaxId(taxId));
        log.debug("find SA result = {}", pdndResource);
        log.trace("find SA end");
        return pdndResource;
    }

}
