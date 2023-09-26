package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Station;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.core.PDNDService;
import it.pagopa.selfcare.party.registry_proxy.web.model.PDNDsResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.PDNDMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/pdnd", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "pdnd")
public class PDNDController {

    private final PDNDService pdndService;
    private final PDNDMapper pdndMapper;

    @Autowired
    public PDNDController(PDNDService pdndService,
                          PDNDMapper pdndMapper) {
        log.trace("Initializing {}", PDNDController.class.getSimpleName());
        this.pdndService = pdndService;
        this.pdndMapper = pdndMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.pdnd.search.summary}",
            notes = "${swagger.api.pdnd.search.notes}")
    public PDNDsResource search(@ApiParam("${swagger.model.pdnd.search}")
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
        final QueryResult<Station> result = pdndService.search(search, page, limit);
        final PDNDsResource pdndResource = PDNDsResource.builder()
                .items(result.getItems().stream()
                        .map(pdndMapper::toResource)
                        .collect(Collectors.toList()))
                .count(result.getTotalHits())
                .build();
        log.debug("search result = {}", pdndResource);
        log.trace("search end");
        return pdndResource;
    }

}
