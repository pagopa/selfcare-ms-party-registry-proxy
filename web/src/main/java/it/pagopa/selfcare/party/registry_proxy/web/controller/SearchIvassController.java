package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.core.IvassAiSearchService;
import it.pagopa.selfcare.party.registry_proxy.core.IvassAzureAiServiceImpl;
import it.pagopa.selfcare.party.registry_proxy.web.model.InsuranceCompaniesResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.InsuranceCompanyResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.InsuranceCompanyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/insurance-companies", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "insurance-companies")
public class SearchIvassController {

    private final IvassAiSearchService ivassService;
    private final InsuranceCompanyMapper insuranceCompanyMapper;

    public SearchIvassController(IvassAzureAiServiceImpl ivassAzureAiService,
                                 InsuranceCompanyMapper insuranceCompanyMapper) {
        this.ivassService = ivassAzureAiService;
        this.insuranceCompanyMapper = insuranceCompanyMapper;
    }

    @GetMapping("/{originId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "${swagger.api.insurance-company.search.byOriginId.summary}",
            description = "${swagger.api.insurance-company.search.byOriginId.notes}",
            operationId = "azureAiSearchInsurancesUsingGET")
    public InsuranceCompanyResource ivassAzureAiSearchByOriginId(@ApiParam("${swagger.model.insurance-company.originId}")
                                                     @PathVariable("originId") String originId) {
        log.trace("ivassAzureAiSearchByOriginId start");
        if (originId.matches("\\w*")) {
            log.debug("ivassAzureAiSearchByOriginId parameter = {}", originId);
        }
        final InsuranceCompanyResource insuranceCompany = insuranceCompanyMapper.toResource(ivassService.findByOriginId(originId));
        log.debug("ivassAzureAiSearchByOriginId result = {}", insuranceCompany);
        log.trace("ivassAzureAiSearchByOriginId end");
        return insuranceCompany;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "${swagger.api.insurance-company.search.summary}",
            description = "${swagger.api.insurance-company.search.notes}",
            operationId = "azureAiSearchInsuranceByIvassCode")
    public InsuranceCompaniesResource ivassAzureAiSearch(@ApiParam("${swagger.model.*.search}")
                                             @RequestParam(value = "search", required = false)
                                             Optional<String> search,
                                                         @ApiParam(value = "${swagger.model.*.page}")
                                             @RequestParam(value = "page", required = false, defaultValue = "1")
                                             Integer page,
                                                         @ApiParam(value = "${swagger.model.*.limit}")
                                             @RequestParam(value = "limit", required = false, defaultValue = "10")
                                             Integer limit) {
        log.trace("ivassAzureAiSearch start");
        log.debug("ivassAzureAiSearch search = {}, page = {}, limit = {}", search, page, limit);
        final QueryResult<InsuranceCompany> result = ivassService.search(search, page, limit);
        final InsuranceCompaniesResource companiesResource = InsuranceCompaniesResource.builder()
                .items(result.getItems().stream()
                        .map(insuranceCompanyMapper::toResource)
                        .collect(Collectors.toList()))
                .count(result.getTotalHits())
                .build();
        log.debug("ivassAzureAiSearch result = {}", companiesResource);
        log.trace("ivassAzureAiSearch end");
        return companiesResource;
    }
}
