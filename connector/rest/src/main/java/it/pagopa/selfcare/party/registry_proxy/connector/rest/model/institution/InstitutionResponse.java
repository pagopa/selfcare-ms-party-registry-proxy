package it.pagopa.selfcare.party.registry_proxy.connector.rest.model.institution;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstitutionResponse {

  private Map<String, Object> institutionResponse;

//  @NotBlank
//  private String id;
//  private String externalId;
//  @NotBlank
//  private String origin;
//  @NotBlank
//  private String originId;
//  private String description;
//  private String institutionType;
//  private String digitalAddress;
//  private String address;
//  private String zipCode;
//  private String taxCode;
//  private String city;
//  private String county;
//  private String country;
//  private String istatCode;
//  private List<GeoTaxonomies> geographicTaxonomies;
//  private List<AttributesResponse> attributes;
//  private List<OnboardedProductResponse> onboarding;
//  private PaymentServiceProviderResponse paymentServiceProvider;
//  private DataProtectionOfficerResponse dataProtectionOfficer;
//  private RootParentResponse rootParent;
//  private String rea;
//  private String shareCapital;
//  private String businessRegisterPlace;
//  private String supportEmail;
//  private String supportPhone;
//  private boolean imported;
//  private String subunitCode;
//  private String subunitType;
//  private String aooParentCode;
//  private OffsetDateTime createdAt;
//  private OffsetDateTime updatedAt;
//  private boolean delegation;
//  private String logo;
//  private Boolean isTest;
//  private String legalForm;

  public String getId() {
    return institutionResponse.get("id").toString();
  }

  public String getDescription() {
    return institutionResponse.get("description").toString();
  }

  public String getInstitutionType() {
    return institutionResponse.get("institutionType").toString();
  }

  public String getDigitalAddress() {
    return institutionResponse.get("digitalAddress").toString();
  }

  public String getTaxCode() {
    return institutionResponse.get("taxCode").toString();
  }

  public List<String> getProducts() {
    List<String> products = new
  }
  public List<Map<String, Object>> getOnboarding() {
    return institutionResponse.get("onboarding");
  }

  public RootParentResponse getRootParent() {
    return rootParent;
  }

  public String getRea() {
    return rea;
  }

  public String getShareCapital() {
    return shareCapital;
  }

  public String getBusinessRegisterPlace() {
    return businessRegisterPlace;
  }

  public String getSupportEmail() {
    return supportEmail;
  }

  public String getSupportPhone() {
    return supportPhone;
  }

  public boolean isImported() {
    return imported;
  }

  public String getSubunitCode() {
    return subunitCode;
  }

  public String getSubunitType() {
    return subunitType;
  }

  public String getAooParentCode() {
    return aooParentCode;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public boolean isDelegation() {
    return delegation;
  }

  public String getLogo() {
    return logo;
  }

  public Boolean getTest() {
    return isTest;
  }

  public String getLegalForm() {
    return legalForm;
  }
}
