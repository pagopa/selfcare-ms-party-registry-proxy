package it.pagopa.selfcare.party.registry_proxy.connector.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.party.registry_proxy.connector.model.InsuranceCompany;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchServiceInsuranceCompanyIVASS;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SearchServiceInsuranceCompanyIVASSRequest extends SearchServiceInsuranceCompanyIVASS {
    @JsonProperty("@search.action")
    String action;

    public static SearchServiceInsuranceCompanyIVASSRequest createFromInsuranceCompanyIVASS(InsuranceCompany insuranceCompany) {
        SearchServiceInsuranceCompanyIVASSRequest searchServiceInstitutionRequest = new SearchServiceInsuranceCompanyIVASSRequest();
        searchServiceInstitutionRequest.setId(insuranceCompany.getId());
        searchServiceInstitutionRequest.setOrigin(insuranceCompany.getOrigin());
        searchServiceInstitutionRequest.setOriginId(insuranceCompany.getOriginId());
        searchServiceInstitutionRequest.setDescription(insuranceCompany.getDescription());
        searchServiceInstitutionRequest.setDigitalAddress(insuranceCompany.getDigitalAddress());
        searchServiceInstitutionRequest.setTaxCode(insuranceCompany.getTaxCode());
        searchServiceInstitutionRequest.setAddress(insuranceCompany.getAddress());
        searchServiceInstitutionRequest.setAction("mergeOrUpload");
        return searchServiceInstitutionRequest;
    }

    public static List<SearchServiceInsuranceCompanyIVASSRequest> createFromInsuranceCompanies(List<InsuranceCompany> insuranceCompanies) {
        List<SearchServiceInsuranceCompanyIVASSRequest> requestList = new ArrayList<>();
        insuranceCompanies.stream().forEach(institution -> {
            SearchServiceInsuranceCompanyIVASSRequest searchServiceInstitutionRequest = createFromInsuranceCompanyIVASS(institution);
            requestList.add(searchServiceInstitutionRequest);
        });
        return requestList;
    }
}
