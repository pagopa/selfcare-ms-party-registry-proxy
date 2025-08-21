package it.pagopa.selfcare.party.registry_proxy.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dapr.Topic;
import io.swagger.annotations.Api;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ServiceUnavailableException;
import it.pagopa.selfcare.party.registry_proxy.core.SearchServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "/dapr")
@Api(tags = "dapr")
public class EventController {
  public static final String RESPONSE_FAILED = "FAILED";
  public static final String RESPONSE_SUCCESS = "SUCCESS";
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final SearchServiceImpl searchService;

  @Autowired
  public EventController(SearchServiceImpl searchService) {
    this.searchService = searchService;
  }

  @PostMapping("/subscribe")
  public List<Map<String, Object>> subscribe() {
    return searchService.subscribe();
  }

  @Topic(name = "SC-Contracts", pubsubName = "selc-eventhub-pubsub")
  @PostMapping("/events")
  public ResponseEntity<Map<String, Object>> handleEvent(@RequestBody Map<String, Object> event) {
    try {
      String institutionId = event.getOrDefault("institutionId", "").toString();
      if (institutionId == null || institutionId.trim().isEmpty()) {
        log.error("Institution ID is missing or empty in event data");
        return createErrorResponse("Missing institution id", "The event must contain 'institutionId' field");
      }

      searchService.indexInstitution(institutionId);
      String eventId = (String) event.get("id");
      return createSuccessResponse(eventId, institutionId, "Event processed and institution indexed successfully");

    } catch (ResourceNotFoundException e) {
      log.error("Error processing event", e);
      return createErrorResponse("Missing institution ID", e.getMessage());
    } catch (ServiceUnavailableException e) {
      log.error("Error processing event", e);
      return createServerErrorResponse("Error processing event", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    catch (Exception e) {
      log.error("Failed to parse event payload: {}", e.getMessage());
      return createErrorResponse("Invalid JSON payload", e.getMessage());
    }

  }

  @NotNull
  private static ResponseEntity<Map<String, Object>> createServerErrorResponse(String error_processing_event, String server_error, HttpStatus internalServerError) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", RESPONSE_FAILED);
    response.put("error", error_processing_event);
    response.put("details", server_error);
    response.put("timestamp", System.currentTimeMillis());
    return ResponseEntity.status(internalServerError).body(response);
  }

  private ResponseEntity<Map<String, Object>> createSuccessResponse(String eventId, String institutionId, String message) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", RESPONSE_SUCCESS);
    response.put("message", message);
    response.put("eventId", eventId);
    response.put("institutionId", institutionId);
    response.put("timestamp", System.currentTimeMillis());

    return ResponseEntity.ok(response);
  }
  private ResponseEntity<Map<String, Object>> createErrorResponse(String error, String details) {
    return createServerErrorResponse(error, details, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @GetMapping("/health")
  public ResponseEntity<Map<String, String>> health() {
    Map<String, String> response = new HashMap<>();
    response.put("status", "healthy");
    response.put("service", "registry-proxy-events");
    response.put("timestamp", String.valueOf(System.currentTimeMillis()));

    return ResponseEntity.ok(response);
  }
}
