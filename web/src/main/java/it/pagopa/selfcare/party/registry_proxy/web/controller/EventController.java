package it.pagopa.selfcare.party.registry_proxy.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import it.pagopa.selfcare.party.registry_proxy.connector.model.SearchDocument;
import it.pagopa.selfcare.party.registry_proxy.core.SearchServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "events", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "events")
public class EventController {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final SearchServiceImpl searchService;

  @Autowired
  public EventController(SearchServiceImpl searchService) {
    this.searchService = searchService;
  }

  @PostMapping("/dapr/subscribe")
  public List<Map<String, Object>> subscribe() {
    List<Map<String, Object>> subscriptions = new ArrayList<>();
    Map<String, Object> subscription = new HashMap<>();
    subscription.put("pubsubname", "eventhub-pubsub");
    subscription.put("topic", "party-proxy");
    subscription.put("route", "/events");
    subscriptions.add(subscription);

    log.info("Dapr subscriptions configured: {}", subscriptions);

    return subscriptions;
  }

  @PostMapping("/events")
  public ResponseEntity<String> handleEvent(@RequestBody Map<String, Object> event) {
    try {
      log.info("Received event: {}", event);

      // Estrai i dati dall'evento
      Object data = event.get("data");
      String eventData = objectMapper.writeValueAsString(data);
      JsonNode eventNode = objectMapper.readTree(eventData);

      // Processa e indicizza i dati
      processAndIndex(eventNode);

      return ResponseEntity.ok("Event processed successfully");

    } catch (Exception e) {
      log.error("Error processing event", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error processing event: " + e.getMessage());
    }
  }

  private void processAndIndex(JsonNode eventData) {
    try {
      SearchDocument document = createSearchDocument(eventData);
      searchService.indexDocument(document);
    } catch (Exception e) {
      log.error("Error processing and indexing event data", e);
    }
  }

  private SearchDocument createSearchDocument(JsonNode eventData) {
    SearchDocument document = new SearchDocument();
    document.setId(UUID.randomUUID().toString());
    document.setTimestamp(new Date());

    if (eventData.has("description")) {
      document.setTitle(eventData.get("description").asText());
    }
    if (eventData.has("taxCode")) {
      document.setContent(eventData.get("taxCode").asText());
    }
    if (eventData.has("category")) {
      document.setCategory(eventData.get("category").asText());
    }
    if (eventData.has("tags") && eventData.get("tags").isArray()) {
      List<String> tags = new ArrayList<>();
      eventData.get("tags").forEach(tag -> tags.add(tag.asText()));
      document.setTags(tags);
    }

    return document;
  }

  @GetMapping("/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("Healthy");
  }

}
