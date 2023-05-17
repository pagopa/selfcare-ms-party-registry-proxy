package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.party.registry_proxy.connector.model.GeographicTaxonomy;
import it.pagopa.selfcare.party.registry_proxy.core.GeographicTaxonomiesService;
import it.pagopa.selfcare.party.registry_proxy.web.model.GeographicTaxonomyResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.GeographicTaxonomyMapper;
import it.pagopa.selfcare.party.registry_proxy.web.utils.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static it.pagopa.selfcare.party.registry_proxy.connector.constant.GenericError.RETRIEVE_GEO_TAXONOMIES_ERROR;
import static it.pagopa.selfcare.party.registry_proxy.connector.constant.GenericError.RETRIEVE_GEO_TAXONOMY_ERROR;


@Slf4j
@RestController
@RequestMapping(value = "/v1/geotaxonomies", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "GeographicTaxonomies")
public class GeographicTaxonomiesController {

    private final GeographicTaxonomiesService geographicTaxonomiesService;

    public GeographicTaxonomiesController(GeographicTaxonomiesService geographicTaxonomiesService) {
        this.geographicTaxonomiesService = geographicTaxonomiesService;
    }

    /**
     * The function returns geographic taxonomies by description
     *
     * @param description String
     * @param offset      Integer
     * @param limit       Integer
     * @return List
     * * Code: 200, Message: successful operation, DataType: GeographicTaxonomies
     * * Code: 404, Message: GeographicTaxonomies not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.registry-proxy.api.geotaxonomies.getGeographicTaxonomiesByDescription}", notes = "${swagger.registry-proxy.api.geotaxonomies.getGeographicTaxonomiesByDescription}")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GeographicTaxonomyResource>> retrieveGeoTaxonomiesByDescription(@ApiParam("${swagger.api.geotaxonomy.model.description}")
                                                                                               @RequestParam(value = "description") String description,
                                                                                               @ApiParam("${swagger.api.geotaxonomy.model.offset}")
                                                                                               @RequestParam(value = "offset", required = false) Integer offset,
                                                                                               @ApiParam("${swagger.api.geotaxonomy.model.limit}")
                                                                                               @RequestParam(value = "limit", required = false) Integer limit) {

        CustomExceptionMessage.setCustomMessage(RETRIEVE_GEO_TAXONOMIES_ERROR);
        List<GeographicTaxonomy> geographicTaxonomyList = geographicTaxonomiesService.retrieveGeoTaxonomiesByDescription(description, offset, limit);
        return ResponseEntity.ok().body(GeographicTaxonomyMapper.toResourceList(geographicTaxonomyList));
    }


    /**
     * The function returns geographic taxonomy by code
     *
     * @param code String
     * @return List
     * * Code: 200, Message: successful operation, DataType: GeographicTaxonomies
     * * Code: 404, Message: GeographicTaxonomies not found, DataType: Problem
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.registry-proxy.api.geotaxonomies.getGeographicTaxonomyByCode}", notes = "${swagger.registry-proxy.api.geotaxonomies.getGeographicTaxonomyByCode}")
    @GetMapping(value = "/{geotaxId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GeographicTaxonomyResource retrieveGeoTaxonomiesByCode(@ApiParam("${swagger.geographicTaxonomy.model.geotaxId}")
                                                                  @PathVariable("geotaxId") String code) {

        CustomExceptionMessage.setCustomMessage(RETRIEVE_GEO_TAXONOMY_ERROR);
        GeographicTaxonomy geographicTaxonomy = geographicTaxonomiesService.retriveGeoTaxonomyByCode(code);
        return GeographicTaxonomyMapper.toResource(geographicTaxonomy);
    }

}
