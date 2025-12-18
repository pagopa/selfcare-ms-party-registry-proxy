package it.pagopa.selfcare.party.registry_proxy.connector.model;


import lombok.Data;

@Data
public class SearchServiceInsuranceCompanyIVASS {

    private String id;
    private String originId;
    private String workType;
    private String registerType;
    private String taxCode;
    private String description;
    private String address;
    private String digitalAddress;
    private Origin origin;

    public SearchServiceInsuranceCompanyIVASS createSearchServiceInsuranceCompany() {
        SearchServiceInsuranceCompanyIVASS searchServiceInstitution = new SearchServiceInsuranceCompanyIVASS();
        searchServiceInstitution.setId(id);
        searchServiceInstitution.setDescription(description);
        searchServiceInstitution.setTaxCode(taxCode);
        searchServiceInstitution.setOrigin(origin);
        searchServiceInstitution.setOriginId(originId);
        searchServiceInstitution.setDigitalAddress(digitalAddress);
        searchServiceInstitution.setAddress(address);
        searchServiceInstitution.setRegisterType(registerType);
        searchServiceInstitution.setWorkType(workType);
        return searchServiceInstitution;
    }

}
