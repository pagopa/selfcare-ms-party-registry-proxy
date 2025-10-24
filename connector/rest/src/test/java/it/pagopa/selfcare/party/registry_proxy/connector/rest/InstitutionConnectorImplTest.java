package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.InstitutionRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.institution.InstitutionResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.institution.OnboardedProductResponse;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.institution.RelationshipState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstitutionConnectorImplTest {

  @InjectMocks
  private InstitutionConnectorImpl institutionConnector;

  @Mock
  private InstitutionRestClient restClient;



  @Test
  void getById_shouldReturnInstitution() {
    // Arrange
    String institutionId = "test-id";
    InstitutionResponse response = createDummyInstitutionResponse();
    when(restClient.getById(institutionId)).thenReturn(response);

    // Act
    Institution result = institutionConnector.getById(institutionId);

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
    Institution result = institutionConnector.getById(institutionId);

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
  private InstitutionResponse createDummyInstitutionResponse() {
    InstitutionResponse response = new InstitutionResponse();
    response.setId("inst-id-1");
    response.setExternalId("ext-id-1");
    response.setOrigin("IPA");
    response.setOriginId("origin-id-1");
    response.setDescription("Test Institution");
    response.setInstitutionType("PA");
    response.setDigitalAddress("test@pec.it");
    response.setAddress("Via Roma 1");
    response.setZipCode("00100");
    response.setTaxCode("12345678901");
    response.setCity("Rome");
    response.setCounty("RM");
    response.setCountry("IT");
    response.setCreatedAt(OffsetDateTime.now());
    response.setUpdatedAt(OffsetDateTime.now());

    OnboardedProductResponse productResponse = new OnboardedProductResponse();
    productResponse.setProductId("prod-io");
    productResponse.setStatus(RelationshipState.ACTIVE);
    productResponse.setTokenId("token-1");

    response.setOnboarding(Collections.singletonList(productResponse));

    return response;
  }
}
