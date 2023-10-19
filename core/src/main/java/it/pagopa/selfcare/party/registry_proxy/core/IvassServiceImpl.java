package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.IndexSearchService;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.core.exception.TooManyResourceFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class IvassServiceImpl implements IvassService {

    private final IndexSearchService<InsuranceCompany> indexSearchService;

    @Autowired
    IvassServiceImpl(IndexSearchService<InsuranceCompany> indexSearchService) {
        log.trace("Initializing {}", IvassServiceImpl.class.getSimpleName());
        this.indexSearchService = indexSearchService;
    }

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

}
