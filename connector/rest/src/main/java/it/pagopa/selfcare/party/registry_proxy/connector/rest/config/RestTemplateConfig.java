package it.pagopa.selfcare.party.registry_proxy.connector.rest.config;

import it.pagopa.selfcare.party.registry_proxy.connector.rest.decoder.RestTemplateErrorHandler;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.interceptor.IvassInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {
    private final IvassInterceptor ivassInterceptor;
    private final RestTemplateErrorHandler restTemplateErrorHandler;

    public RestTemplateConfig(IvassInterceptor ivassInterceptor, RestTemplateErrorHandler restTemplateErrorHandler) {
        this.ivassInterceptor = ivassInterceptor;
        this.restTemplateErrorHandler = restTemplateErrorHandler;
    }

    @Bean
    public RestTemplate ivassRestTemplate() {
        CookieStore cookieStore = new BasicCookieStore();

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCookieStore(cookieStore)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(List.of(ivassInterceptor));
        restTemplate.setErrorHandler(restTemplateErrorHandler);
        return restTemplate;
    }

}
