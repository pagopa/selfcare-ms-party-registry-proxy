package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AzureSearchStatus;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AzureSearchValue;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Onboarding;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.AzureSearchRestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AzureSearchConnectorImpl.class})
@ExtendWith(SpringExtension.class)
public class AzureSearchConnectorImplTest {

  @Autowired
  private AzureSearchConnectorImpl azureSearchConnector;

  @MockBean
  private AzureSearchRestClient azureSearchRestClient;

  @Test
  public void testIndexInstitution() {
    AzureSearchStatus azureSearchStatus = new AzureSearchStatus();
    AzureSearchValue azureSearchValue = new AzureSearchValue();
    azureSearchValue.setStatus(true);
    azureSearchValue.setStatusCode(200);
    azureSearchStatus.setValue(List.of(azureSearchValue));

    Institution institution = new Institution();
    institution.setId("1");
    institution.setDescription("Institution");
    institution.setTaxCode("taxcode");
    Onboarding onboarding = new Onboarding();
    onboarding.setProductId("prod1");
    onboarding.setInstitutionType(InstitutionType.PA);
    institution.setOnboarding(List.of(onboarding));
    institution.setUpdatedAt(OffsetDateTime.now());

    when(azureSearchConnector.indexInstitution(institution)).thenReturn(azureSearchStatus);
    AzureSearchStatus azureResponse = azureSearchConnector.indexInstitution(institution);
    assertEquals(1, azureResponse.getValue().size());
    assertEquals(200, azureResponse.getValue().get(0).getStatusCode());
  }

}
