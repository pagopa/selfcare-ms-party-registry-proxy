package it.pagopa.selfcare.party.registry_proxy.connector.rest.decoder;

import it.pagopa.selfcare.party.registry_proxy.connector.exception.InternalException;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.InvalidRequestException;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Configuration
public class RestTemplateErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        switch (response.getStatusCode()) {
            case BAD_REQUEST:
                throw new InvalidRequestException(response.getStatusText());
            case NOT_FOUND:
                throw new ResourceNotFoundException(response.getStatusText());
            default:
                throw new InternalException(response.getStatusText());
        }
    }
}
