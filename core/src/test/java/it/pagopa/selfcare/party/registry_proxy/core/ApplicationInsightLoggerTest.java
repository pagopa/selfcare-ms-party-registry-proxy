package it.pagopa.selfcare.party.registry_proxy.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dapr.client.DaprClient;
import io.dapr.client.domain.InvokeBindingRequest;
import io.dapr.utils.TypeRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationInsightsLoggerTest {

  @Mock
  private DaprClient mockDaprClient;

  private ApplicationInsightsLogger applicationInsightsLogger;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    applicationInsightsLogger = new ApplicationInsightsLogger();
    objectMapper = new ObjectMapper();

    // Inject mocked dependencies using reflection
    ReflectionTestUtils.setField(applicationInsightsLogger, "daprClient", mockDaprClient);
    ReflectionTestUtils.setField(applicationInsightsLogger, "objectMapper", objectMapper);
    ReflectionTestUtils.setField(applicationInsightsLogger, "appInsightsBindingName", "test-binding");
    ReflectionTestUtils.setField(applicationInsightsLogger, "applicationName", "test-app");
    ReflectionTestUtils.setField(applicationInsightsLogger, "appInsightsLoggingEnabled", true);
    ReflectionTestUtils.setField(applicationInsightsLogger, "connectionString", "InstrumentationKey=test-key;IngestionEndpoint=https://test.com/");
  }

  @Test
  void testLogEventProcessingSuccess() throws Exception {

    // Given
    String eventType = "TestEventSuccess";
    String eventId = "event-123";
    String institutionId = "inst-456";
    long processingTimeMs = 1500L;

    // Mock Dapr client response
    when(mockDaprClient.invokeBinding(any(InvokeBindingRequest.class), any(TypeRef.class)))
      .thenReturn(Mono.empty());
    // When
    applicationInsightsLogger.logEventProcessingSuccess(eventType, eventId, institutionId, processingTimeMs);

    // Wait a moment for async processing
    Thread.sleep(100);

    // Then
    ArgumentCaptor<InvokeBindingRequest> requestCaptor = ArgumentCaptor.forClass(InvokeBindingRequest.class);
    verify(mockDaprClient).invokeBinding(requestCaptor.capture(), any(TypeRef.class));

    InvokeBindingRequest capturedRequest = requestCaptor.getValue();
    assertEquals("test-binding", capturedRequest.getName());
    assertEquals("create", capturedRequest.getOperation());

    // Parse the sent JSON payload
    byte[] requestCaptorData = (byte[]) requestCaptor.getValue().getData();
    String jsonPayload = new String(requestCaptorData, 0, requestCaptorData.length, StandardCharsets.UTF_8);
    List<Map<String, Object>> telemetryItems = objectMapper.readValue(jsonPayload, new TypeReference<List<Map<String, Object>>>() {});

    assertFalse(telemetryItems.isEmpty());
    Map<String, Object> telemetryItem = telemetryItems.get(0);

    assertEquals("Microsoft.ApplicationInsights.Event", telemetryItem.get("name"));
    assertNotNull(telemetryItem.get("time"));
    assertEquals("test-key", telemetryItem.get("iKey"));

    @SuppressWarnings("unchecked")
    Map<String, Object> data = (Map<String, Object>) telemetryItem.get("data");
    assertEquals("EventData", data.get("baseType"));

    @SuppressWarnings("unchecked")
    Map<String, Object> baseData = (Map<String, Object>) data.get("baseData");
    assertEquals(eventType, baseData.get("name"));
    assertEquals(1, baseData.get("version"));

    @SuppressWarnings("unchecked")
    Map<String, String> properties = (Map<String, String>) baseData.get("properties");
    assertEquals(eventId, properties.get("eventId"));
    assertEquals(institutionId, properties.get("institutionId"));
    assertEquals("Information", properties.get("severityLevel"));

    @SuppressWarnings("unchecked")
    Map<String, Double> measurements = (Map<String, Double>) baseData.get("measurements");
    assertEquals(1500.0, measurements.get("processingTimeMs"));
  }

  @Test
  void testLogEventProcessingErrorWithException() throws Exception {
    // Given
    String eventType = "TestEventError";
    String eventId = "event-error-123";
    String institutionId = "inst-error-456";
    String error = "Test error";
    String details = "Error details";
    RuntimeException exception = new RuntimeException("Test exception message");

    // Mock Dapr client response
    when(mockDaprClient.invokeBinding(any(InvokeBindingRequest.class), any(TypeRef.class)))
      .thenReturn(Mono.empty());
    // When
    applicationInsightsLogger.logEventProcessingError(eventType, eventId, institutionId, error, details, exception);

    // Wait a moment for async processing
    Thread.sleep(100);

    // Then
    ArgumentCaptor<InvokeBindingRequest> requestCaptor = ArgumentCaptor.forClass(InvokeBindingRequest.class);
    verify(mockDaprClient).invokeBinding(requestCaptor.capture(), any(TypeRef.class));

    byte[] requestCaptorData = (byte[]) requestCaptor.getValue().getData();
    String jsonPayload = new String(requestCaptorData, 0, requestCaptorData.length, StandardCharsets.UTF_8);
    List<Map<String, Object>> telemetryItems = objectMapper.readValue(jsonPayload, new TypeReference<List<Map<String, Object>>>() {});

    Map<String, Object> telemetryItem = telemetryItems.get(0);
    assertEquals("Microsoft.ApplicationInsights.Exception", telemetryItem.get("name"));

    @SuppressWarnings("unchecked")
    Map<String, Object> data = (Map<String, Object>) telemetryItem.get("data");
    assertEquals("ExceptionData", data.get("baseType"));

    @SuppressWarnings("unchecked")
    Map<String, Object> baseData = (Map<String, Object>) data.get("baseData");
    assertEquals(1, baseData.get("version"));
    assertEquals("Error", baseData.get("severityLevel"));

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> exceptions = (List<Map<String, Object>>) baseData.get("exceptions");
    assertFalse(exceptions.isEmpty());

    Map<String, Object> exceptionDetails = exceptions.get(0);
    assertEquals(1, exceptionDetails.get("id"));
    assertEquals(0, exceptionDetails.get("outerId"));
    assertEquals("java.lang.RuntimeException", exceptionDetails.get("typeName"));
    assertEquals("Test exception message", exceptionDetails.get("message"));
    assertTrue((Boolean) exceptionDetails.get("hasFullStack"));
    assertNotNull(exceptionDetails.get("stack"));

    @SuppressWarnings("unchecked")
    Map<String, String> properties = (Map<String, String>) baseData.get("properties");
    assertEquals(eventId, properties.get("eventId"));
    assertEquals(institutionId, properties.get("institutionId"));
    assertEquals(error, properties.get("error"));
    assertEquals(details, properties.get("details"));
    assertEquals(eventType, properties.get("eventType"));
  }

  @Test
  void testLogEventProcessingErrorWithoutException() throws Exception {
    // Given
    String eventType = "TestEventError";
    String eventId = "event-error-123";
    String institutionId = "inst-error-456";
    String error = "Test error";
    String details = "Error details";

    // Mock Dapr client response
    when(mockDaprClient.invokeBinding(any(InvokeBindingRequest.class), any(TypeRef.class)))
      .thenReturn(Mono.empty());
    // When
    applicationInsightsLogger.logEventProcessingError(eventType, eventId, institutionId, error, details, null);

    // Wait a moment for async processing
    Thread.sleep(100);

    // Then
    ArgumentCaptor<InvokeBindingRequest> requestCaptor = ArgumentCaptor.forClass(InvokeBindingRequest.class);
    verify(mockDaprClient).invokeBinding(requestCaptor.capture(), any(TypeRef.class));

    byte[] requestCaptorData = (byte[]) requestCaptor.getValue().getData();
    String jsonPayload = new String(requestCaptorData, 0, requestCaptorData.length, StandardCharsets.UTF_8);
    List<Map<String, Object>> telemetryItems = objectMapper.readValue(jsonPayload, new TypeReference<List<Map<String, Object>>>() {});


    Map<String, Object> telemetryItem = telemetryItems.get(0);
    assertEquals("Microsoft.ApplicationInsights.Event", telemetryItem.get("name"));

    @SuppressWarnings("unchecked")
    Map<String, Object> data = (Map<String, Object>) telemetryItem.get("data");
    assertEquals("EventData", data.get("baseType"));

    @SuppressWarnings("unchecked")
    Map<String, Object> baseData = (Map<String, Object>) data.get("baseData");
    assertEquals(eventType, baseData.get("name"));

    @SuppressWarnings("unchecked")
    Map<String, String> properties = (Map<String, String>) baseData.get("properties");
    assertEquals(eventId, properties.get("eventId"));
    assertEquals(institutionId, properties.get("institutionId"));
    assertEquals(error, properties.get("error"));
    assertEquals(details, properties.get("details"));
    assertEquals("Error", properties.get("severityLevel"));
  }

  @Test
  void testLogEventProcessingWarning() throws Exception {
    // Given
    String eventId = "warning-event-123";
    String institutionId = "warning-inst-456";
    String warning = "Test warning";
    String reason = "Warning reason";

    // Mock Dapr client response
    when(mockDaprClient.invokeBinding(any(InvokeBindingRequest.class), any(TypeRef.class)))
      .thenReturn(Mono.empty());
    // When
    applicationInsightsLogger.logEventProcessingWarning(eventId, institutionId, warning, reason);

    // Wait a moment for async processing
    Thread.sleep(100);

    // Then
    ArgumentCaptor<InvokeBindingRequest> requestCaptor = ArgumentCaptor.forClass(InvokeBindingRequest.class);
    verify(mockDaprClient).invokeBinding(requestCaptor.capture(), any(TypeRef.class));

    byte[] requestCaptorData = (byte[]) requestCaptor.getValue().getData();
    String jsonPayload = new String(requestCaptorData, 0, requestCaptorData.length, StandardCharsets.UTF_8);
    List<Map<String, Object>> telemetryItems = objectMapper.readValue(jsonPayload, new TypeReference<List<Map<String, Object>>>() {});

    Map<String, Object> telemetryItem = telemetryItems.get(0);
    assertEquals("Microsoft.ApplicationInsights.Event", telemetryItem.get("name"));

    @SuppressWarnings("unchecked")
    Map<String, Object> data = (Map<String, Object>) telemetryItem.get("data");
    @SuppressWarnings("unchecked")
    Map<String, Object> baseData = (Map<String, Object>) data.get("baseData");
    assertEquals("IndexInstitutionProcessingWarning", baseData.get("name"));

    @SuppressWarnings("unchecked")
    Map<String, String> properties = (Map<String, String>) baseData.get("properties");
    assertEquals(eventId, properties.get("eventId"));
    assertEquals(institutionId, properties.get("institutionId"));
    assertEquals(warning, properties.get("warning"));
    assertEquals(reason, properties.get("reason"));
    assertEquals("Warning", properties.get("severityLevel"));
  }

  @Test
  void testLogCustomEvent() throws Exception {
    // Given
    String eventName = "CustomTestEvent";
    Map<String, Object> properties = Map.of(
      "prop1", "value1",
      "prop2", 123,
      "prop3", true
    );
    Map<String, Double> measurements = Map.of(
      "metric1", 100.5,
      "metric2", 200.0
    );

    // Mock Dapr client response
    when(mockDaprClient.invokeBinding(any(InvokeBindingRequest.class), any(TypeRef.class)))
      .thenReturn(Mono.empty());

    // When
    applicationInsightsLogger.logCustomEvent(eventName, properties, measurements);

    // Wait a moment for async processing
    Thread.sleep(100);

    // Then
    ArgumentCaptor<InvokeBindingRequest> requestCaptor = ArgumentCaptor.forClass(InvokeBindingRequest.class);
    verify(mockDaprClient).invokeBinding(requestCaptor.capture(), any(TypeRef.class));

    byte[] requestCaptorData = (byte[]) requestCaptor.getValue().getData();
    String jsonPayload = new String(requestCaptorData, 0, requestCaptorData.length, StandardCharsets.UTF_8);
    List<Map<String, Object>> telemetryItems = objectMapper.readValue(jsonPayload, new TypeReference<List<Map<String, Object>>>() {});

    Map<String, Object> telemetryItem = telemetryItems.get(0);
    assertEquals("Microsoft.ApplicationInsights.Event", telemetryItem.get("name"));

    @SuppressWarnings("unchecked")
    Map<String, Object> data = (Map<String, Object>) telemetryItem.get("data");
    @SuppressWarnings("unchecked")
    Map<String, Object> baseData = (Map<String, Object>) data.get("baseData");
    assertEquals(eventName, baseData.get("name"));

    @SuppressWarnings("unchecked")
    Map<String, String> capturedProperties = (Map<String, String>) baseData.get("properties");
    assertEquals("value1", capturedProperties.get("prop1"));
    assertEquals("123", capturedProperties.get("prop2"));
    assertEquals("true", capturedProperties.get("prop3"));

    @SuppressWarnings("unchecked")
    Map<String, Double> capturedMeasurements = (Map<String, Double>) baseData.get("measurements");
    assertEquals(100.5, capturedMeasurements.get("metric1"));
    assertEquals(200.0, capturedMeasurements.get("metric2"));
  }

  @Test
  void testLogPerformanceMetric() throws Exception {
    // Given
    String operation = "TestOperation";
    long durationMs = 2500L;
    boolean success = true;
    Map<String, String> additionalProperties = Map.of("environment", "test");
    ArgumentCaptor<InvokeBindingRequest> requestCaptor = ArgumentCaptor.forClass(InvokeBindingRequest.class);

    // Mock Dapr client response
    when(mockDaprClient.invokeBinding(any(InvokeBindingRequest.class), any(TypeRef.class)))
      .thenReturn(Mono.empty());
    // When
    applicationInsightsLogger.logPerformanceMetric(operation, durationMs, success, additionalProperties);

    // Wait a moment for async processing
    Thread.sleep(100);

    // Then
    verify(mockDaprClient).invokeBinding(requestCaptor.capture(), any(TypeRef.class));

    byte[] requestCaptorData = (byte[]) requestCaptor.getValue().getData();
    String jsonPayload = new String(requestCaptorData, 0, requestCaptorData.length, StandardCharsets.UTF_8);
    List<Map<String, Object>> telemetryItems = objectMapper.readValue(jsonPayload, new TypeReference<List<Map<String, Object>>>() {});

    Map<String, Object> telemetryItem = telemetryItems.get(0);

    @SuppressWarnings("unchecked")
    Map<String, Object> data = (Map<String, Object>) telemetryItem.get("data");
    @SuppressWarnings("unchecked")
    Map<String, Object> baseData = (Map<String, Object>) data.get("baseData");
    assertEquals("PerformanceIndexMetric", baseData.get("name"));

    @SuppressWarnings("unchecked")
    Map<String, String> properties = (Map<String, String>) baseData.get("properties");
    assertEquals(operation, properties.get("operation"));
    assertEquals("true", properties.get("success"));
    assertEquals("Information", properties.get("severityLevel"));
    assertEquals("test", properties.get("environment"));

    @SuppressWarnings("unchecked")
    Map<String, Double> measurements = (Map<String, Double>) baseData.get("measurements");
    assertEquals(2500.0, measurements.get("durationMs"));
  }

  @Test
  void testLoggingDisabled() {
    // Given
    ReflectionTestUtils.setField(applicationInsightsLogger, "appInsightsLoggingEnabled", false);

    // When
    applicationInsightsLogger.logEventProcessingSuccess("TestEvent", "event-123", "inst-456", 1000L);

    // Then
    verify(mockDaprClient, never()).invokeBinding(any(), any());
  }

  @Test
  void testDaprClientException() {
    // Given
    when(mockDaprClient.invokeBinding(any(InvokeBindingRequest.class), any(TypeRef.class)))
      .thenReturn(Mono.error(new RuntimeException("Dapr error")));

    // When - should not throw exception due to async error handling
    assertDoesNotThrow(() -> {
      applicationInsightsLogger.logEventProcessingSuccess("TestEvent", "event-123", "inst-456", 1000L);
      Thread.sleep(200); // Wait for async processing
    });

    // Then
    verify(mockDaprClient, times(1)).invokeBinding(any(), any());
  }

  @Test
  void testNullParameters() throws Exception {
    // When
    applicationInsightsLogger.logEventProcessingSuccess("TestEvent", null, null, 1000L);

    // Wait a moment for async processing
    Thread.sleep(100);

    // Then
    ArgumentCaptor<InvokeBindingRequest> requestCaptor = ArgumentCaptor.forClass(InvokeBindingRequest.class);
    verify(mockDaprClient).invokeBinding(requestCaptor.capture(), any(TypeRef.class));

    byte[] requestCaptorData = (byte[]) requestCaptor.getValue().getData();
    String jsonPayload = new String(requestCaptorData, 0, requestCaptorData.length, StandardCharsets.UTF_8);
    List<Map<String, Object>> telemetryItems = objectMapper.readValue(jsonPayload, new TypeReference<List<Map<String, Object>>>() {});

    Map<String, Object> telemetryItem = telemetryItems.get(0);
    @SuppressWarnings("unchecked")
    Map<String, Object> data = (Map<String, Object>) telemetryItem.get("data");
    @SuppressWarnings("unchecked")
    Map<String, Object> baseData = (Map<String, Object>) data.get("baseData");
    @SuppressWarnings("unchecked")
    Map<String, String> properties = (Map<String, String>) baseData.get("properties");

    assertNull(properties.get("eventId"));
    assertNull(properties.get("institutionId"));
  }

  @Test
  void testExtractInstrumentationKey() {
    // Given
    ReflectionTestUtils.setField(applicationInsightsLogger, "connectionString",
      "InstrumentationKey=abc-123-def;IngestionEndpoint=https://test.com/");

    // When
    String key = ReflectionTestUtils.invokeMethod(applicationInsightsLogger, "extractInstrumentationKey");

    // Then
    assertEquals("abc-123-def", key);
  }

  @Test
  void testExtractInstrumentationKeyWithNullConnectionString() {
    // Given
    ReflectionTestUtils.setField(applicationInsightsLogger, "connectionString", null);

    // When
    String key = ReflectionTestUtils.invokeMethod(applicationInsightsLogger, "extractInstrumentationKey");

    // Then
    assertEquals("default-key", key);
  }

  @Test
  void testExtractInstrumentationKeyWithInvalidConnectionString() {
    // Given
    ReflectionTestUtils.setField(applicationInsightsLogger, "connectionString", "InvalidConnectionString");

    // When
    String key = ReflectionTestUtils.invokeMethod(applicationInsightsLogger, "extractInstrumentationKey");

    // Then
    assertEquals("default-key", key);
  }

  @Test
  void testGetStackTrace() {
    // Given
    Exception rootCause = new IllegalArgumentException("Root cause message");
    Exception wrappedException = new RuntimeException("Wrapper exception", rootCause);

    // When
    String stackTrace = ReflectionTestUtils.invokeMethod(applicationInsightsLogger, "getStackTrace", wrappedException);

    // Then
    assertNotNull(stackTrace);
    assertTrue(stackTrace.contains("Wrapper exception"));
    assertTrue(stackTrace.contains("Root cause message"));
    assertTrue(stackTrace.contains("Caused by:"));
  }

  @Test
  void testGetStackTraceWithNullException() {
    // When
    String stackTrace = ReflectionTestUtils.invokeMethod(applicationInsightsLogger, "getStackTrace", (Throwable) null);

    // Then
    assertNull(stackTrace);
  }

  @Test
  void testTelemetryItemStructure() throws Exception {
    // Mock Dapr client response
    when(mockDaprClient.invokeBinding(any(InvokeBindingRequest.class), any(TypeRef.class)))
      .thenReturn(Mono.empty());
    // Given
    applicationInsightsLogger.logEventProcessingSuccess("TestEvent", "event-123", "inst-456", 1000L);

    // Wait for async processing
    Thread.sleep(100);

    // Then
    ArgumentCaptor<InvokeBindingRequest> requestCaptor = ArgumentCaptor.forClass(InvokeBindingRequest.class);
    verify(mockDaprClient).invokeBinding(requestCaptor.capture(), any(TypeRef.class));

    byte[] requestCaptorData = (byte[]) requestCaptor.getValue().getData();
    String jsonPayload = new String(requestCaptorData, 0, requestCaptorData.length, StandardCharsets.UTF_8);
    List<Map<String, Object>> telemetryItems = objectMapper.readValue(jsonPayload, new TypeReference<List<Map<String, Object>>>() {});

    Map<String, Object> telemetryItem = telemetryItems.get(0);

    // Verify required Application Insights fields
    assertNotNull(telemetryItem.get("name"));
    assertNotNull(telemetryItem.get("time"));
    assertNotNull(telemetryItem.get("iKey"));
    assertNotNull(telemetryItem.get("data"));
    assertNotNull(telemetryItem.get("tags"));

    @SuppressWarnings("unchecked")
    Map<String, String> tags = (Map<String, String>) telemetryItem.get("tags");
    assertNotNull(tags.get("ai.session.id"));
    assertNotNull(tags.get("ai.operation.id"));
    assertEquals("1.0.0", tags.get("ai.application.ver"));
    assertEquals("test-app", tags.get("ai.cloud.role"));
    assertEquals("java:dapr:1.0.0", tags.get("ai.internal.sdkVersion"));
  }
}