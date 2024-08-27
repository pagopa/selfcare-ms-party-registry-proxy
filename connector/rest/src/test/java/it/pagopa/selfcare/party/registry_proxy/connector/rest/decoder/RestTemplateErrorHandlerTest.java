package it.pagopa.selfcare.party.registry_proxy.connector.rest.decoder;

import it.pagopa.selfcare.party.registry_proxy.connector.exception.InternalException;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.InvalidRequestException;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RestTemplateErrorHandlerTest {
    @Mock
    private ClientHttpResponse response;

    @InjectMocks
    private RestTemplateErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void hasError_shouldReturnTrue_whenResponseIsError() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        assertTrue(errorHandler.hasError(response));
    }

    @Test
    void hasError_shouldReturnFalse_whenResponseIsNotError() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.OK);
        assertFalse(errorHandler.hasError(response));
    }

    @Test
    void handleError_shouldThrowInvalidRequestException_whenStatusIsBadRequest() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(response.getStatusText()).thenReturn("Bad Request");
        assertThrows(InvalidRequestException.class, () -> errorHandler.handleError(response));
    }

    @Test
    void handleError_shouldThrowResourceNotFoundException_whenStatusIsNotFound() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        when(response.getStatusText()).thenReturn("Not Found");
        assertThrows(ResourceNotFoundException.class, () -> errorHandler.handleError(response));
    }

    @Test
    void handleError_shouldThrowInternalException_whenStatusIsOtherError() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(response.getStatusText()).thenReturn("Internal Server Error");
        assertThrows(InternalException.class, () -> errorHandler.handleError(response));
    }
}