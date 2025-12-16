package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchServiceInstitutionIPA;
import it.pagopa.selfcare.party.registry_proxy.core.SearchInstitutionService;
import it.pagopa.selfcare.party.registry_proxy.web.model.InstitutionResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.InstitutionsResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.InstitutionMapper;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.InstitutionsMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/ai-search/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "institution")
public class SearchInstitutionController {

    private final SearchInstitutionService institutionService;

    @Autowired
    public SearchInstitutionController(SearchInstitutionService institutionService) {
        log.trace("Initializing {}", InstitutionController.class.getSimpleName());
        this.institutionService = institutionService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "${swagger.api.institution.search.summary}",
            description = "${swagger.api.institution.search.notes}",
            operationId = "searchInstitutionsUsingGET")
    public InstitutionsResource search(@ApiParam("${swagger.model.institution.search}")
                                       @RequestParam(value = "search", required = false)
                                       Optional<String> search,
                                       @ApiParam(value = "${swagger.model.*.page}")
                                       @RequestParam(value = "page", required = false, defaultValue = "1")
                                       Integer page,
                                       @ApiParam(value = "${swagger.model.*.limit}")
                                       @RequestParam(value = "limit", required = false, defaultValue = "10")
                                       Integer limit,
                                       @ApiParam(value = "${swagger.model.*.categories}")
                                       @RequestParam(value = "categories", required = false)
                                       String categories) {
        log.trace("search start");
        log.debug("search search = {}, page = {}, limit = {}", search, page, limit);
        final List<SearchServiceInstitutionIPA> result = categories == null ? institutionService.search(search, page, limit)
                : institutionService.search(search, categories, page, limit);
        final InstitutionsResource institutionsResource = InstitutionsMapper.toResource(result.stream()
                        .map(InstitutionMapper::toResource)
                        .collect(Collectors.toList()),
                100);
        log.debug("search result = {}", institutionsResource);
        log.trace("search end");
        return institutionsResource;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "${swagger.api.institution.findInstitution.summary}",
            description = "${swagger.api.institution.findInstitution.notes}",
            operationId = "findInstitutionUsingGET")
    public InstitutionResource findInstitution(@ApiParam("${swagger.api.institution.findInstitution.param.id}")
                                               @PathVariable("id") String id,
                                               @ApiParam("${swagger.model.*.origin}")
                                               @RequestParam(value = "origin", required = false) Optional<Origin> origin,
                                               @ApiParam(value = "${swagger.model.*.categories}")
                                               @RequestParam(value = "categories", required = false)
                                               Optional<String> categories) {
        log.trace("findInstitution start");
        log.debug("findInstitution id = {}, origin = {}, categories = {}", id, origin, categories);
        List<String> categoriesList = new ArrayList<>();
        if (categories.isPresent()) {
            categoriesList = Arrays.stream(categories.get().split(",")).collect(Collectors.toList());
        }
        final InstitutionResource institutionResource = InstitutionMapper.toResource(institutionService.findById(id,
                origin.isEmpty() ? Optional.of(Origin.IPA) : origin, categoriesList));

        log.debug("findInstitution result = {}", institutionResource);
        log.trace("findInstitution end");
        return institutionResource;
    }

}
