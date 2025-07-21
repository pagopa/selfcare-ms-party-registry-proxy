package it.pagopa.selfcare.party.registry_proxy.core;

import it.pagopa.selfcare.party.registry_proxy.connector.model.nationalregistriespdnd.PDNDBusiness;

import java.util.List;

public interface PDNDInfoCamereService {

    List<PDNDBusiness> retrieveInstitutionsPdndByDescription(String description);

    PDNDBusiness retrieveInstitutionPdndByTaxCode(String taxCode);

    PDNDBusiness retrieveInstitutionDetail(String taxCode);

    byte[] retrieveInstitutionDocument(String taxCode);

    PDNDBusiness retrieveInstitutionFromRea(String county, String rea);

}
