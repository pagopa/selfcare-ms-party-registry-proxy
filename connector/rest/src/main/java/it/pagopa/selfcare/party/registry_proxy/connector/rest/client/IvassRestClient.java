package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import it.pagopa.selfcare.party.registry_proxy.connector.rest.decoder.RestTemplateErrorHandler;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.interceptor.IvassInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@PropertySource("classpath:config/ivass-rest-config.properties")
public class IvassRestClient {
    public static final String GET_INSURANCES_PATH = "/RIGAInquiry-public/getAreaDownloadExport.do?referenceDate=&product=VFLUSSO_IMPRESE&language=IT&exportType=CSV&isCompressed=S";
    private final RestTemplate ivassRestTemplate;
    private final IvassInterceptor ivassInterceptor;
    private final RestTemplateErrorHandler restTemplateErrorHandler;
    private final String ivassBasePath;

    public IvassRestClient(
            @Value("${ivass.rest.endpoint}") String ivassBasePath,
            IvassInterceptor ivassInterceptor,
            RestTemplateErrorHandler restTemplateErrorHandler) {
        this.ivassInterceptor = ivassInterceptor;
        this.restTemplateErrorHandler = restTemplateErrorHandler;
        this.ivassRestTemplate = configureIvassRestTemplate();
        this.ivassBasePath = ivassBasePath;
    }

    private RestTemplate configureIvassRestTemplate() {
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

    public byte[] getInsurancesZip() {
        log.info("getInsurances start");
        String apiPath = this.ivassBasePath + GET_INSURANCES_PATH;
        return ivassRestTemplate.getForObject(apiPath, byte[].class);
    }
}
