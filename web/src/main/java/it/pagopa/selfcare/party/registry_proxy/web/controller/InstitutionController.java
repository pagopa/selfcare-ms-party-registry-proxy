package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Institution;
import it.pagopa.selfcare.party.registry_proxy.web.model.InstitutionResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.InstitutionsResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.InstitutionMapper;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.InstitutionsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v1/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "institution")
public class InstitutionController {

    private final IndexSearchService<Institution> indexSearchService;


    @Autowired
    public InstitutionController(IndexSearchService<Institution> indexSearchService) {
        this.indexSearchService = indexSearchService;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.institution.search.summary}",
            notes = "${swagger.api.institution.search.notes}")
    public InstitutionsResource search(@ApiParam("${swagger.model.institution.search}")
                                       @RequestParam(value = "search", required = false)
                                               String search,
                                       @ApiParam(value = "${swagger.model.institution.page}")
                                       @RequestParam(value = "page", required = false, defaultValue = "1")
                                               String page,
                                       @ApiParam(value = "${swagger.model.institution.limit}")
                                       @RequestParam(value = "limit", required = false, defaultValue = "10")
                                               String limit) {
        final List<Institution> institutions = indexSearchService.searchByText("description", search, Integer.parseInt(page), Integer.parseInt(limit));
        return InstitutionsMapper.toResource(institutions.stream()
                .map(InstitutionMapper::toResource)
                .collect(Collectors.toList()));
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.institution.findInstitution.summary}",
            notes = "${swagger.api.institution.findInstitution.notes}")
    public InstitutionResource findInstitution(@ApiParam("${swagger.api.institution.findInstitution.param.id}")
                                               @PathVariable("id") String id,
                                               @ApiParam("${swagger.model.institution.origin}")
                                               @RequestParam(value = "origin", required = false) String origin) {
        return null;
    }

}
