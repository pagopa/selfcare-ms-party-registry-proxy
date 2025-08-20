package it.pagopa.selfcare.party.registry_proxy.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dapr.client.DaprClient;

import it.pagopa.selfcare.party.registry_proxy.connector.api.InstitutionConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Onboarding;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService{

  private final DaprClient daprClient;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final InstitutionConnector institutionConnector;

  @Autowired
  public SearchServiceImpl(DaprClient daprClient, InstitutionConnector institutionConnector) {
    this.daprClient = daprClient;
    this.institutionConnector = institutionConnector;
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Override
  public void indexInstitution(String institutionId) {
    Institution institution = institutionConnector.getById(institutionId);
    InstitutionIndex institutionIndex = InstitutionIndex.createFromInstitution(institution);
    String institutionToIndex = null;
    try {
      institutionToIndex = objectMapper.writeValueAsString(institutionIndex);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    Optional.ofNullable(institutionToIndex).ifPresent(value -> {
      daprClient.invokeBinding("selc-http-search-binding", "create", value).retry(10).block();
      log.info("Document {} indexed.", value);
    });
  }

  @Data
  static class InstitutionIndex {
    String id;
    String description;
    String taxCode;
    List<String> products;
    List<String> institutionTypes;
    OffsetDateTime lastModified;

    static InstitutionIndex createFromInstitution(Institution institution) {
      InstitutionIndex institutionIndex = new InstitutionIndex();
      institutionIndex.setId(institution.getId());
      institutionIndex.setDescription(institution.getDescription());
      institutionIndex.setTaxCode(institution.getTaxCode());
      institutionIndex.setProducts(institution.getOnboarding().stream().map(Onboarding::getProductId).toList());
      institutionIndex.setInstitutionTypes(institution.getOnboarding().stream().map(onboarding -> onboarding.getInstitutionType().toString()).toList());
      institutionIndex.setLastModified(institution.getUpdatedAt());
      return institutionIndex;
    }
  }
}

