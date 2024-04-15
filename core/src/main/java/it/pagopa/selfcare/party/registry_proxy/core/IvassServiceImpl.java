package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.connector.model.Entity;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.core.exception.TooManyResourceFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IvassServiceImpl implements IvassService {

    private final IndexSearchService<InsuranceCompany> indexSearchService;

    @Autowired
    IvassServiceImpl(IndexSearchService<InsuranceCompany> indexSearchService) {
        log.trace("Initializing {}", IvassServiceImpl.class.getSimpleName());
        this.indexSearchService = indexSearchService;
    }

    /**
     * @deprecated method has been deprecated because a new method has been implemented.
     */
    @Deprecated(forRemoval = true)
    @Override
    public InsuranceCompany findByTaxCode(String taxId) {
        log.trace("findByTaxCode start");
        log.debug("findByTaxCode parameter = {}", taxId.toUpperCase());
        final List<InsuranceCompany> companies = indexSearchService.findById(InsuranceCompany.Field.ID, taxId.toUpperCase());
        if (companies.isEmpty()) {
            throw new ResourceNotFoundException();
        } else if (companies.size() > 1) {
            throw new TooManyResourceFoundException();
        }
        final InsuranceCompany company = companies.get(0);
        log.debug("findByTaxCode result = {}", company);
        log.trace("findByTaxCode end");
        return company;
    }

    @Override
    public QueryResult<InsuranceCompany> search(Optional<String> searchText, int page, int limit) {
        log.trace("search start");
        log.debug("search searchText = {}, page = {}, limit = {}", searchText, page, limit);
        final QueryResult<InsuranceCompany> queryResult = searchText.map(text -> indexSearchService.fullTextSearch(InsuranceCompany.Field.DESCRIPTION, text, page, limit))
                .orElseGet(() -> indexSearchService.findAll(page, limit, Entity.INSURANCE_COMPANY.toString()));
        log.debug("search result = {}", queryResult);
        log.trace("search end");
        return queryResult;
    }

}
