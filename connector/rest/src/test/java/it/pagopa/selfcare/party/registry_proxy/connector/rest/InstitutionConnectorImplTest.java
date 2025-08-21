package it.pagopa.selfcare.party.registry_proxy.connector.rest;

import it.pagopa.selfcare.party.registry_proxy.connector.rest.cache.CacheConfig;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.client.InstitutionRestClient;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.institution.InstitutionResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CacheConfig.class})
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:config/selc-institution-rest-client.properties")
@SpringBootTest
@EnableCaching
public class InstitutionConnectorImplTest {

  @InjectMocks
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
