package it.pagopa.selfcare.party.registry_proxy.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.pagopa.selfcare.party.registry_proxy.connector.model.national_registries_pdnd.PDNDBusiness;

import java.util.List;

public interface PDNDInfoCamereService {

    List<PDNDBusiness> retrieveInstitutionsPdndByDescription(String description);

    PDNDBusiness retrieveInstitutionPdndByTaxCode(String taxCode) throws JsonProcessingException;

    PDNDBusiness retrieveInstitutionDetail(String taxCode);

    byte[] retrieveInstitutionDocument(String taxCode);

    PDNDBusiness retrieveInstitutionFromRea(String county, String rea);

}
