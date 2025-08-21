package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.InstitutionRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.institution.InstitutionResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {InstitutionConnectorImpl.class})
@ExtendWith(SpringExtension.class)
public class InstitutionConnectorImplTest {

  @Autowired
  private InstitutionConnectorImpl institutionConnector;

  @MockBean
  private InstitutionRestClient institutionRestClient;

  @Test
  void testGetById() {
    InstitutionResponse institutionResponse = new InstitutionResponse();
    institutionResponse.setId("1");
    when(institutionRestClient.getById("1")).thenReturn(institutionResponse);
    Assertions.assertEquals("1", institutionRestClient.getById("1").getId());
  }
}
