package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.party.registry_proxy.core.IvassService;
import it.pagopa.selfcare.party.registry_proxy.web.model.InsuranceCompanyResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.InsuranceCompanyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{taxId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.insurance-company.search.byId.summary}", notes = "${swagger.api.insurance-company.search.byId.notes}")
    public InsuranceCompanyResource searchByTaxCode(@ApiParam("${swagger.model.insurance-company.taxCode}")
                                                    @PathVariable("taxId") String taxId) {
        log.trace("searchByTaxCode start");
        log.debug("searchByTaxCode parameter = {}", taxId);
        final InsuranceCompanyResource insuranceCompany = insuranceCompanyMapper.toResource(ivassService.findByTaxCode(taxId));
        log.debug("searchByTaxCode result = {}", insuranceCompany);
        log.trace("searchByTaxCode end");
        return insuranceCompany;
    }
}
