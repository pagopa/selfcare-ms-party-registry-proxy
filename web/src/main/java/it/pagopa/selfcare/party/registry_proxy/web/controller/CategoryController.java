package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import it.pagopa.selfcare.party.registry_proxy.core.NameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Api(tags = "institution")
public class CategoryController {

    private final NameService nameService;//TODO change Name


    @Autowired
    public CategoryController(NameService nameService) {
        this.nameService = nameService;
    }

}
