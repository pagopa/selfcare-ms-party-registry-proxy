package it.pagopa.selfcare.party.registry_proxy.connector.rest.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import feign.CollectionFormat;
import feign.RequestTemplate;
import org.junit.jupiter.api.Test;

class InfoCamereInterceptorTest {
    /**
     * Method under test: {@link InfoCamereInterceptor#apply(RequestTemplate)}
     */
    @Test
    void testApply() {
        InfoCamereInterceptor infoCamereInterceptor = new InfoCamereInterceptor();
        RequestTemplate requestTemplate = new RequestTemplate();
        infoCamereInterceptor.apply(requestTemplate);
        assertFalse(requestTemplate.resolved());
        assertEquals("/", requestTemplate.path());
        assertTrue(requestTemplate.decodeSlash());
        assertEquals(CollectionFormat.EXPLODED, requestTemplate.collectionFormat());
    }
}

