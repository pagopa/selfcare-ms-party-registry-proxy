package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.connector.model.QueryResult;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.IvassAzureSearchRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.IvassQueryResultImpl;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.IvassSearchServiceResponse;
import it.pagopa.selfcare.party.registry_proxy.core.exception.TooManyResourceFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.party.registry_proxy.connector.rest.utils.Const.*;
@Slf4j
@Service
public class IvassAzureAiServiceImpl implements IvassAiSearchService {

    private final IvassAzureSearchRestClient ivassAzureSearchRestClient;

    public IvassAzureAiServiceImpl(IvassAzureSearchRestClient ivassAzureSearchRestClient) {
        this.ivassAzureSearchRestClient = ivassAzureSearchRestClient;
    }

    @Override
    public QueryResult<InsuranceCompany> search(Optional<String> searchText, int page, int limit) {
        log.trace("search start");
        log.debug("search searchText = {}, page = {}, limit = {}", searchText, page, limit);
        int skip = (page - 1) * limit;

        final IvassSearchServiceResponse queryResult = searchText
                .map(text -> ivassAzureSearchRestClient.search(
                        IVASS_INDEX_NAME,
                        INDEX_API_VERSION,
                        text+"*",
                        InsuranceCompany.Field.DESCRIPTION.name(),
                        null,
                        skip,
                        limit,
                        true
                        ))
                .orElseGet(() -> ivassAzureSearchRestClient.search(IVASS_INDEX_NAME, INDEX_API_VERSION,"*", null,null, skip, limit, true));

        log.debug("search result = {}", queryResult);
        log.trace("search end");
        return new IvassQueryResultImpl(queryResult);
    }

    @Override
    public InsuranceCompany findByOriginId(String originId) {
        log.trace("findByIvassCode start");
        IvassSearchServiceResponse searchServiceResponse = ivassAzureSearchRestClient.search(
                IVASS_INDEX_NAME,
                INDEX_API_VERSION,
                null,
                null,
                String.format("%s eq '%s'",
                        InsuranceCompany.Field.ORIGIN_ID,
                        originId.toUpperCase()
                ),
                null,
                null,
                false
        );

        List<InsuranceCompany> companies = new ArrayList<>();
        Optional.of(searchServiceResponse).ifPresent(response -> companies.addAll(response.getValue()));
        if (companies.isEmpty()) {
            throw new ResourceNotFoundException();
        } else if (companies.size() > 1) {
            throw new TooManyResourceFoundException();
        }
        final InsuranceCompany company = companies.get(0);
        log.debug("findByIvassCode result = {}", company);
        log.trace("findByIvassCode end");
        return company;
    }
}
