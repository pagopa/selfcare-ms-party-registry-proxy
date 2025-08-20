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
@RequestMapping(value = "/dapr")
@Api(tags = "dapr")
public class EventController {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final SearchServiceImpl searchService;

  @Autowired
  public EventController(SearchServiceImpl searchService) {
    this.searchService = searchService;
  }

  @PostMapping("/subscribe")
  public List<Map<String, Object>> subscribe() {
    List<Map<String, Object>> subscriptions = new ArrayList<>();
    Map<String, Object> subscription = new HashMap<>();
    subscription.put("pubsubname", "eventhub-pubsub");
    subscription.put("topic", "party-proxy");
    subscription.put("route", "/dapr/events");
    subscriptions.add(subscription);

    log.info("Dapr subscriptions configured: {}", subscriptions);

    return subscriptions;
  }

  @PostMapping("/events")
  public ResponseEntity<String> handleEvent(@RequestBody Map<String, Object> event) {
    try {
      log.info("Received event: {}", event);

      String institutionId = event.getOrDefault("institutionId", "").toString();
      searchService.indexInstitution(institutionId);

      return ResponseEntity.ok("Event processed successfully");

    } catch (Exception e) {
      log.error("Error processing event", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error processing event: " + e.getMessage());
    }
  }



  @GetMapping("/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("Healthy");
  }
}
