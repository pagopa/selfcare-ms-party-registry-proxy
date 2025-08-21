package it.pagopa.selfcare.party.registry_proxy.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dapr.client.DaprClient;
import it.pagopa.selfcare.party.registry_proxy.connector.api.AzureSearchConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InstitutionConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ServiceUnavailableException;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AzureSearchStatus;
import it.pagopa.selfcare.party.registry_proxy.connector.model.AzureSearchValue;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Onboarding;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

  private final DaprClient daprClient;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final InstitutionConnector institutionConnector;
  private final AzureSearchConnector azureSearchConnector;

  @Value("${dapr.queue.binding-name}")
  private String queueBindingName;

  @Value("${dapr.queue.topic}")
  private String kafkaTopic;

  @Autowired
  public SearchServiceImpl(DaprClient daprClient, InstitutionConnector institutionConnector, AzureSearchConnector azureSearchConnector) {
    this.daprClient = daprClient;
    this.institutionConnector = institutionConnector;
    this.azureSearchConnector = azureSearchConnector;
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Override
  public List<Map<String, Object>> subscribe() {
    List<Map<String, Object>> subscriptions = new ArrayList<>();
    Map<String, Object> subscription = new HashMap<>();
    subscription.put("pubsubname", queueBindingName);
    subscription.put("topic", "kafkaTopic");
    subscription.put("route", "/dapr/events");
    subscriptions.add(subscription);

    log.info("Dapr subscriptions configured: {}", subscriptions);
    return subscriptions;
  }

  @Override
  public boolean indexInstitution(String institutionId) {
    Institution institution = institutionConnector.getById(institutionId);
    if (institution == null) {
      throw new ResourceNotFoundException();
    }
    AzureSearchStatus status = azureSearchConnector.indexInstitution(institution);

    if (status == null || status.getValue() == null || status.getValue().isEmpty()) {
      throw new ServiceUnavailableException();
    }

    for (AzureSearchValue value : status.getValue()) {
      log.debug("Indexing status for institution {}: {}", institutionId, value.getStatus());
      if (!value.getStatus()) {
        throw new ServiceUnavailableException();
      }
    }
    return true;
  }
}

