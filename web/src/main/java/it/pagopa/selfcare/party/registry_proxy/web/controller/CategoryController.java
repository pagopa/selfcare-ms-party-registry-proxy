package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Category;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Origin;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.core.CategoryService;
import it.pagopa.selfcare.party.registry_proxy.web.model.CategoriesResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.CategoryResource;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.CategoriesMapper;
import it.pagopa.selfcare.party.registry_proxy.web.model.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "category")
public class CategoryController {

    private final CategoryService categoryService;


    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("categories")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.category.findCategories.summary}",
            notes = "${swagger.api.category.findCategories.notes}")
    public CategoriesResource findCategories(@ApiParam("${swagger.model.*.origin}")
                                             @RequestParam(value = "origin", required = false) Optional<Origin> origin,
                                             @ApiParam(value = "${swagger.model.*.page}")
                                             @RequestParam(value = "page", required = false, defaultValue = "1")
                                                     Integer page,
                                             @ApiParam(value = "${swagger.model.*.limit}")
                                             @RequestParam(value = "limit", required = false, defaultValue = "10")
                                                     Integer limit) {
        final QueryResult<Category> result = categoryService.search(origin, page, limit);
        return CategoriesMapper.toResource(result.getItems().stream()
                .map(CategoryMapper::toResource)
                .collect(Collectors.toList()));
    }


    @GetMapping("origins/{origin}/categories/{code}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.api.category.findCategory.summary}",
            notes = "${swagger.api.category.findCategory.notes}")
    public CategoryResource findCategory(@ApiParam("${swagger.model.*.origin}")
                                         @PathVariable("origin") Origin origin,
                                         @ApiParam("${swagger.model.category.code}")
                                         @PathVariable("code") String code) {
        return CategoryMapper.toResource(categoryService.findById(code, origin));
    }

}
