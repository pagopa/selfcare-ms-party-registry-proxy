package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.cache.CacheConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.InstitutionRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.institution.InstitutionResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.institution.OnboardedProductResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.institution.RelationshipState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CacheConfig.class})
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:config/selc-institution-rest-client.properties")
@SpringBootTest
@EnableCaching
public class InstitutionConnectorImplTest {

  public static final String INSTITUTION_ID = "inst-id-123";
  @InjectMocks
  private InstitutionConnectorImpl institutionConnector;

  @Mock
  private InstitutionRestClient restClient;

  @Test
  void testGetById() {
    Map<String, Object> response = createDummyInstitutionResponse();

    when(restClient.getById(INSTITUTION_ID)).thenReturn(response);
    assertEquals(INSTITUTION_ID, restClient.getById(INSTITUTION_ID).get("id"));
  }

  @Test
  void getById_shouldReturnInstitution() {
    // Arrange
    String institutionId = "test-id";
    InstitutionResponse response = createDummyInstitutionResponse();
    when(restClient.getById(institutionId)).thenReturn(response);

    // Act
    Map<String, Object> result = institutionConnector.getById(institutionId);

    // Assert
    assertNotNull(result);
    assertEquals(response.getId(), result.getId());
    assertEquals(response.getExternalId(), result.getExternalId());
    assertEquals(response.getDescription(), result.getDescription());
    assertEquals(response.getTaxCode(), result.getTaxCode());
    assertEquals(response.getOnboarding().get(0).getProductId(), result.getOnboarding().get(0).getProductId());
    assertEquals(response.getOnboarding().get(0).getStatus().name(), result.getOnboarding().get(0).getStatus());

    verify(restClient, times(1)).getById(institutionId);
    verifyNoMoreInteractions(restClient);
  }

  @Test
  void getById_shouldReturnInstitution_whenOnboardingIsNull() {
    // Arrange
    String institutionId = "test-id-no-onboarding";
    InstitutionResponse response = createDummyInstitutionResponse();
    response.setOnboarding(null); // Simulate null onboarding list
    when(restClient.getById(institutionId)).thenReturn(response);

    // Act
    Map<String, Object> result = institutionConnector.getById(institutionId);

    // Assert
    assertNotNull(result);
    assertEquals(response.getId(), result.getId());
    // Verify that the onboarding list is not null but an empty list in the final object
    assertNotNull(result.getOnboarding());
    assertTrue(result.getOnboarding().isEmpty());

    verify(restClient, times(1)).getById(institutionId);
    verifyNoMoreInteractions(restClient);
  }

  /**
   * Helper method to create a populated InstitutionResponse object for testing.
   */
//  private InstitutionResponse createDummyInstitutionResponse() {
//    InstitutionResponse response = new InstitutionResponse();
//    response.setId("inst-id-1");
//    response.setExternalId("ext-id-1");
//    response.setOrigin("IPA");
//    response.setOriginId("origin-id-1");
//    response.setDescription("Test Institution");
//    response.setInstitutionType("PA");
//    response.setDigitalAddress("test@pec.it");
//    response.setAddress("Via Roma 1");
//    response.setZipCode("00100");
//    response.setTaxCode("12345678901");
//    response.setCity("Rome");
//    response.setCounty("RM");
//    response.setCountry("IT");
//    response.setCreatedAt(OffsetDateTime.now());
//    response.setUpdatedAt(OffsetDateTime.now());
//
//    OnboardedProductResponse productResponse = new OnboardedProductResponse();
//    productResponse.setProductId("prod-io");
//    productResponse.setStatus(RelationshipState.ACTIVE);
//    productResponse.setTokenId("token-1");
//
//    response.setOnboarding(Collections.singletonList(productResponse));
//
//    return response;
//  }

  /**
   * Creates a Map simulating the JSON response for an institution.
   * @return A Map<String, Object> with test data.
   */
  public static Map<String, Object> createDummyInstitutionResponse() {
    // Main object
    Map<String, Object> institutionMap = new java.util.HashMap<>();
    institutionMap.put("id", INSTITUTION_ID);
    institutionMap.put("externalId", "ext-id-123");
    institutionMap.put("origin", "IPA");
    institutionMap.put("originId", "c_h501");
    institutionMap.put("description", "Comune di Roma");
    institutionMap.put("institutionType", "PA");
    institutionMap.put("digitalAddress", "protocollo.comuneroma@pec.it");
    institutionMap.put("address", "Piazza del Campidoglio, 1");
    institutionMap.put("zipCode", "00186");
    institutionMap.put("taxCode", "01234567890");
    institutionMap.put("city", "Roma");
    institutionMap.put("county", "RM");
    institutionMap.put("country", "IT");
    institutionMap.put("createdAt", OffsetDateTime.now().toString());
    institutionMap.put("updatedAt", OffsetDateTime.now().toString());

    // Nested onboarding object
    Map<String, Object> onboardingMap = new java.util.HashMap<>();
    onboardingMap.put("productId", "prod-io");
    onboardingMap.put("status", "ACTIVE");

    // Nested billing object inside onboarding
    Map<String, Object> billingMap = new java.util.HashMap<>();
    billingMap.put("vatNumber", "01234567890");
    billingMap.put("recipientCode", "XYZ123");

    onboardingMap.put("billing", billingMap);

    // Add the onboarding object to a list
    institutionMap.put("onboarding", List.of(onboardingMap));

    return institutionMap;
  }
}
}
