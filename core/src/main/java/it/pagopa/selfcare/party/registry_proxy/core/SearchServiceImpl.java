package it.pagopa.selfcare.party.registry_proxy.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dapr.client.DaprClient;

import io.dapr.client.domain.InvokeBindingRequest;
import io.dapr.utils.TypeRef;
import it.pagopa.selfcare.party.registry_proxy.connector.api.InstitutionConnector;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Institution;
import it.pagopa.selfcare.party.registry_proxy.connector.model.institution.Onboarding;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

  private final DaprClient daprClient;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final InstitutionConnector institutionConnector;

  @Value("${azure.search.binding.name}")
  private String searchBindingName;

  @Value("${azure.search.service.name}")
  private String searchServiceName;

  @Value("${azure.search.index.name}")
  private String indexName;

  @Autowired
  public SearchServiceImpl(DaprClient daprClient, InstitutionConnector institutionConnector) {
    this.daprClient = daprClient;
    this.institutionConnector = institutionConnector;
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Override
  public List<Map<String, Object>> subscribe() {
    List<Map<String, Object>> subscriptions = new ArrayList<>();
    Map<String, Object> subscription = new HashMap<>();
    subscription.put("pubsubname", "selc-eventhub-pubsub");
    subscription.put("topic", "SC-Contracts");
    subscription.put("route", "/dapr/events");
    subscriptions.add(subscription);

    log.info("Dapr subscriptions configured: {}", subscriptions);
    return subscriptions;
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
      daprClient.invokeBinding("selc-http-search-binding", "post", value).retry(1).block();
//      //indexInstitution(institutionId, value)
//      checkIndexExists()
//        .doOnSuccess(result -> log.info("Successfully indexed institution: {}", institutionId))
//        .doOnError(error -> log.error("Failed to index institution {}: {}", institutionId, error.getMessage()))
//        .onErrorResume(throwable -> {
//          // Gestione degli errori senza bloccare l'applicazione
//          log.warn("Indexing failed for institution {}, continuing...", institutionId);
//          return Mono.empty();
//        })
//        .subscribe();
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

  /**
   * Indicizza un'istituzione in Azure Search
   */
  public Mono<Void> indexInstitution(String institutionId, Object institutionData) {
    return Mono.fromCallable(() -> {
      try {
        log.info("Starting indexing process for institution: {}", institutionId);

        Map<String, Object> document = Map.of(
          "@search.action", "mergeOrUpload",
          "id", institutionId,
          "data", institutionData
        );

        Map<String, Object> requestBody = Map.of(
          "value", new Object[]{document}
        );

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        InvokeBindingRequest request = new InvokeBindingRequest("selc-http-search-binding", "post")
          .setData(jsonBody.getBytes())
          .setMetadata(Map.of(
            "path", "/indexes/institution-index/docs/index",
            "api-version", "2023-11-01"
          ));

        // Invoca il binding
        daprClient.invokeBinding(request, TypeRef.get(Void.class)).block();

        log.info("Institution {} indexed successfully", institutionId);
        return null;

      } catch (Exception e) {
        log.error("Error indexing institution {}: {}", institutionId, e.getMessage(), e);
        throw new RuntimeException("Failed to index institution", e);
      }
    });
  }

  public Mono<Boolean> testConnection() {
    return Mono.fromCallable(() -> {
      try {
        log.info("Testing connection to Azure Search service: {}", searchServiceName);

        // Test semplice: ottenere informazioni sul servizio
        InvokeBindingRequest request = new InvokeBindingRequest(searchBindingName, "get")
          .setMetadata(Map.of(
            "path", "/servicestats",
            "api-version", "2021-04-30-Preview"
          ));

        byte[] response = daprClient.invokeBinding(request, TypeRef.get(byte[].class)).block();

        if (response != null) {
          String responseStr = new String(response);
          log.info("Connection test successful. Response: {}", responseStr);
          return true;
        } else {
          log.warn("Connection test returned null response");
          return false;
        }

      } catch (Exception e) {
        log.error("Connection test failed: {}", e.getMessage(), e);
        return false;
      }
    });
  }

  /**
   * Verifica se l'indice esiste
   */
  public Mono<Boolean> checkIndexExists() {
    return Mono.fromCallable(() -> {
      try {
        log.info("Checking if index '{}' exists", indexName);

        InvokeBindingRequest request = new InvokeBindingRequest(searchBindingName, "get")
          .setMetadata(Map.of(
            "path", "/indexes/" + indexName,
            "api-version", "2021-04-30-Preview"
          ));

        byte[] response = daprClient.invokeBinding(request, TypeRef.get(byte[].class)).block();

        if (response != null) {
          log.info("Index '{}' exists", indexName);
          return true;
        } else {
          log.warn("Index '{}' does not exist", indexName);
          return false;
        }

      } catch (Exception e) {
        if (e.getMessage().contains("404")) {
          log.warn("Index '{}' not found", indexName);
          return false;
        } else {
          log.error("Error checking index '{}': {}", indexName, e.getMessage(), e);
          throw new RuntimeException("Failed to check index", e);
        }
      }
    });
  }
}

