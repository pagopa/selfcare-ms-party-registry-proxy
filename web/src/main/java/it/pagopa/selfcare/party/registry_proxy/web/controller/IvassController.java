package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.core.IvassService;
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
public class IvassController {

    private final IvassService ivassService;
    private final InsuranceCompanyMapper insuranceCompanyMapper;

    public IvassController(IvassService ivassService,
                           InsuranceCompanyMapper insuranceCompanyMapper) {
        this.ivassService = ivassService;
        this.insuranceCompanyMapper = insuranceCompanyMapper;
    }

    /**
     * @deprecated since a new version has been implemented
     */
    @Deprecated(forRemoval = true)
    @GetMapping("/{taxId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.insurance-company.search.byId.summary}", notes = "${swagger.api.insurance-company.search.byId.notes}")
    public InsuranceCompanyResource searchByTaxCode(@ApiParam("${swagger.model.insurance-company.taxCode}")
                                                    @PathVariable("taxId") String taxId) {
        log.trace("searchByTaxCode start");
        if (taxId.matches("\\w*")) {
            log.debug("searchByTaxCode parameter = {}", taxId);
        }
        final InsuranceCompanyResource insuranceCompany = insuranceCompanyMapper.toResource(ivassService.findByTaxCode(taxId));
        log.debug("searchByTaxCode result = {}", insuranceCompany);
        log.trace("searchByTaxCode end");
        return insuranceCompany;
    }

    @GetMapping("/origin/{originId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.insurance-company.search.byIvassCode.summary}", notes = "${swagger.api.insurance-company.search.byIvassCode.notes}")
    public InsuranceCompanyResource searchByOriginId(@ApiParam("${swagger.model.insurance-company.originId}")
                                                     @PathVariable("originId") String originId) {
        log.trace("searchByOriginId start");
        if (originId.matches("\\w*")) {
            log.debug("searchByOriginId parameter = {}", originId);
        }
        final InsuranceCompanyResource insuranceCompany = insuranceCompanyMapper.toResource(ivassService.findByOriginId(originId));
        log.debug("searchByOriginId result = {}", insuranceCompany);
        log.trace("searchByOriginId end");
        return insuranceCompany;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.insurance-company.search.summary}", notes = "${swagger.api.insurance-company.search.notes}")
    public InsuranceCompaniesResource search(@ApiParam("${swagger.model.*.search}")
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
        final QueryResult<InsuranceCompany> result = ivassService.search(search, page, limit);
        final InsuranceCompaniesResource companiesResource = InsuranceCompaniesResource.builder()
                .items(result.getItems().stream()
                        .map(insuranceCompanyMapper::toResource)
                        .collect(Collectors.toList()))
                .count(result.getTotalHits())
                .build();
        log.debug("search result = {}", companiesResource);
        log.trace("search end");
        return companiesResource;
    }
}
