package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import io.github.resilience4j.retry.annotation.Retry;
import it.pagopa.selfcare.party.registry_proxy.connector.api.NationalRegistriesConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.PDNDNationalRegistriesConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistries.*;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.NationalRegistriesRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.PDNDNationalRegistriesRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.*;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.mapper.PDNDBusinessMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
@Service
public class PDNDNationalRegistriesConnectorImpl implements PDNDNationalRegistriesConnector {

    private final PDNDNationalRegistriesRestClient pdndNationalRegistriesRestClient;
    private final PDNDBusinessMapper pdndBusinessMapper;

    public PDNDNationalRegistriesConnectorImpl(PDNDNationalRegistriesRestClient pdndNationalRegistriesRestClient, PDNDBusinessMapper pdndBusinessMapper) {
        this.pdndNationalRegistriesRestClient = pdndNationalRegistriesRestClient;
        this.pdndBusinessMapper = pdndBusinessMapper;
    }

    @Override
    @Retry(name = "retryServiceUnavailable")
    public List<PDNDBusiness> institutionsPdndByDescription(String description) {
        return pdndBusinessMapper.toPDNDBusiness(pdndNationalRegistriesRestClient.getInstitutionPdndbyDescription(description));
    }

}
