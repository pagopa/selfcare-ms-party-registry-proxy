package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.party.registry_proxy.core.NameService;
import it.pagopa.selfcare.party.registry_proxy.web.model.InstitutionResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.InstitutionsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "institution")
public class InstitutionController {

    private final NameService nameService;//TODO change Name


    @Autowired
    public InstitutionController(NameService nameService) {
        this.nameService = nameService;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.institution.findInstitutions.summary}",
            notes = "${swagger.api.institution.findInstitutions.notes}")
    public InstitutionsResource findInstitutions(@ApiParam("${swagger.model.institution.search}")
                                                 @RequestParam(value = "search", required = false)
                                                         String search,
                                                 @ApiParam(value = "${swagger.model.institution.page}", defaultValue = "1")
                                                 @RequestParam(value = "page", required = false)
                                                         String page,
                                                 @ApiParam(value = "${swagger.model.institution.limit}", defaultValue = "10")
                                                 @RequestParam(value = "limit", required = false)
                                                         String limit) {
        return null;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.institution.findInstitution.summary}",
            notes = "${swagger.api.institution.findInstitution.notes}")
    public InstitutionResource findInstitution(@ApiParam("${swagger.model.institution.id}")
                                               @PathVariable("id") String id) {
        return null;
    }

}
