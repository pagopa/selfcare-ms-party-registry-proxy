package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;

import java.util.List;

public interface PDNDNationalRegistriesService {

    List<PDNDBusiness> institutionsPdndByDescription(String description);

}
