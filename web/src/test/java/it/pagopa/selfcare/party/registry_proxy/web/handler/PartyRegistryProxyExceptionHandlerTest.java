package it.pagopa.selfcare.party.registry_proxy.web.handler;

import feign.FeignException;
import it.pagopa.selfcare.commons.web.model.Problem;
import it.pagopa.selfcare.party.registry_proxy.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class PartyRegistryProxyExceptionHandlerTest {

    private static final String DETAIL_MESSAGE = "detail message";
    private final PartyRegistryProxyExceptionHandler handler = new PartyRegistryProxyExceptionHandler();

    @Test
    void handleResourceNotFoundException() {
        //given
        ResourceNotFoundException mockException = Mockito.mock(ResourceNotFoundException.class);
        Mockito.when(mockException.getMessage())
                .thenReturn(DETAIL_MESSAGE);
        //when
        ResponseEntity<Problem> responseEntity = handler.handleResourceNotFoundException(mockException);
        //then
        assertNotNull(responseEntity);
        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(DETAIL_MESSAGE, responseEntity.getBody().getDetail());
        assertEquals(NOT_FOUND.value(), responseEntity.getBody().getStatus());
    }


    @Test
    void handleResourceFoundException() {
        FeignException exception = Mockito.mock(FeignException.class);
        Mockito.when(exception.status())
                .thenReturn(404);
        ResponseEntity<Problem> responseEntity = handler.handleFeignException(exception);
        assertNotNull(responseEntity);
        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void handleResourceFoundException2() {
        FeignException exception = Mockito.mock(FeignException.class);
        Mockito.when(exception.status())
                .thenReturn(500);
        ResponseEntity<Problem> responseEntity = handler.handleFeignException(exception);
        assertNotNull(responseEntity);
        assertEquals(INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

}