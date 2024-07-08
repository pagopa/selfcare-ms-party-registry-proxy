package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.api.PDNDNationalRegistriesConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
@Service
class PDNDNationalRegistriesServiceImpl implements PDNDNationalRegistriesService {

    private final PDNDNationalRegistriesConnector pdndNationalRegistriesConnector;

    @Autowired
    PDNDNationalRegistriesServiceImpl(PDNDNationalRegistriesConnector pdndNationalRegistriesConnector) {
        this.pdndNationalRegistriesConnector = pdndNationalRegistriesConnector;
    }


    @Override
    public List<PDNDBusiness> retrieveInstitutionsPdndByDescription(String description){
        Assert.hasText(description, "Description is required");
        return pdndNationalRegistriesConnector.retrieveInstitutionsPdndByDescription(description);
    }

}
