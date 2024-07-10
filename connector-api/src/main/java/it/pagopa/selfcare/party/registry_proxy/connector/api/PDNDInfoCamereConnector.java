package it.pagopa.selfcare.party.registry_proxy.connector.api;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;

import java.util.List;

public interface PDNDInfoCamereConnector {

    List<PDNDBusiness> retrieveInstitutionsPdndByDescription(String description);

    PDNDBusiness retrieveInstitutionPdndByTaxCode(String taxCode);

}
