package it.pagopa.selfcare.party.registry_proxy.connector.model;


import lombok.Data;

@Data
public class SearchServiceInstitutionIPA {

    private String id;
    private String description;
    private String originId;
    private String descriptionFull;
    private String taxCode;
    private String category;
    private String aoo;
    private String uo;
    private String origin;
    private String istatCode;
    private String digitalAddress;
    private String address;
    private String zipCode;

    public SearchServiceInstitutionIPA createSearchServiceInstitution() {
        SearchServiceInstitutionIPA searchServiceInstitution = new SearchServiceInstitutionIPA();
        searchServiceInstitution.setId(id);
        searchServiceInstitution.setDescription(description);
        searchServiceInstitution.setDescriptionFull(descriptionFull);
        searchServiceInstitution.setTaxCode(taxCode);
        searchServiceInstitution.setAoo(aoo);
        searchServiceInstitution.setUo(uo);
        searchServiceInstitution.setCategory(category);
        searchServiceInstitution.setOrigin(origin);
        searchServiceInstitution.setOriginId(originId);
        searchServiceInstitution.setIstatCode(istatCode);
        searchServiceInstitution.setDigitalAddress(digitalAddress);
        searchServiceInstitution.setAddress(address);
        searchServiceInstitution.setZipCode(zipCode);
        return searchServiceInstitution;
    }

}
