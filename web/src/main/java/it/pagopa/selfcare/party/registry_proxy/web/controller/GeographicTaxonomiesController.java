package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GeographicTaxonomies;
import it.pagopa.selfcare.party.registry_proxy.core.GeographicTaxonomiesService;
import it.pagopa.selfcare.party.registry_proxy.web.utils.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static it.pagopa.selfcare.party.registry_proxy.connector.constant.GenericError.RETRIEVE_GEO_TAXONOMIES_ERROR;


@Slf4j
@RestController
@Api(tags = "GeographicTaxonomies")
public class GeographicTaxonomiesController {

    private final GeographicTaxonomiesService geographicTaxonomiesService;

    public GeographicTaxonomiesController(GeographicTaxonomiesService geographicTaxonomiesService) {
        this.geographicTaxonomiesService = geographicTaxonomiesService;
    }

    /**
     * The function returns geographic taxonomies
     *
     * @param description String
     * @return List
     * * Code: 200, Message: successful operation, DataType: GeographicTaxonomies
     * * Code: 404, Message: GeographicTaxonomies not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.external.geotaxonomies.description}", notes = "${swagger.api.external.geotaxonomies.description}")
    @GetMapping(value = "/geotaxonomies")
    public ResponseEntity<List<GeographicTaxonomies>> retrieveGeoTaxonomiesByDescription(@ApiParam("${swagger.api.geotaxonomies.model.description}")
                                                                                         @RequestParam(value = "description") String description,
                                                                                         @ApiParam("${swagger.api.geotaxonomies.model.offset}")
                                                                                         @RequestParam(value = "offset", required = false) Integer offset,
                                                                                         @ApiParam("${swagger.api.geotaxonomies.model.limit}")
                                                                                         @RequestParam(value = "limit", required = false) Integer limit) {

        CustomExceptionMessage.setCustomMessage(RETRIEVE_GEO_TAXONOMIES_ERROR);
        List<GeographicTaxonomies> list = geographicTaxonomiesService.retrieveGeoTaxonomiesByDescription(description, offset, limit);
        return ResponseEntity.ok(list);
    }

}
