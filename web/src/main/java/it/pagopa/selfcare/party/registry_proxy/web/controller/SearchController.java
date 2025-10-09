package it.pagopa.selfcare.party.registry_proxy.web.controller;

import io.swagger.annotations.Api;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchServiceInstitution;
import it.pagopa.selfcare.party.registry_proxy.core.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/search")
@Api(tags = "search")
public class SearchController {

  private final SearchService searchService;

  public SearchController(SearchService searchService) {
    this.searchService = searchService;
  }

  @GetMapping("/institutions")
  @PreAuthorize("hasPermission(new it.pagopa.selfcare.party.registry_proxy.web.security.FilterAuthorityDomain('PAGOPA'), 'Selc:SearchInstitutions')")
  public ResponseEntity<List<SearchServiceInstitution>> searchInstitutions(
    @RequestParam(required = false) List<String> products,
    @RequestParam(required = false) List<String> institutionTypes,
    @RequestParam(required = false) String taxCode,
    @RequestParam(defaultValue = "*") String searchText,
    @RequestParam(defaultValue = "50") int top,
    @RequestParam(defaultValue = "0") int skip) {

    try {
      List<SearchServiceInstitution> institutions = searchService.searchInstitution(searchText, products, institutionTypes, taxCode, top, skip, null, null);

      return ResponseEntity.ok(institutions);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
    }
  }
}
