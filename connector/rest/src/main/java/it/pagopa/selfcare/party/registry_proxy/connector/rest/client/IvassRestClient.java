package it.pagopa.selfcare.party.registry_proxy.connector.rest.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@PropertySource("classpath:config/ivass-rest-config.properties")
public class IvassRestClient {
    public static final String GET_INSURANCES_PATH = "/RIGAInquiry-public/getAreaDownloadExport.do?referenceDate=&product=VFLUSSO_IMPRESE&language=IT&exportType=CSV&isCompressed=S";
    private final RestTemplate ivassRestTemplate;
    private final String ivassBasePath;

    public IvassRestClient(
            @Value("${ivass.rest.endpoint}") String ivassBasePath,
            RestTemplate ivassRestTemplate) {
        this.ivassRestTemplate = ivassRestTemplate;
        this.ivassBasePath = ivassBasePath;
    }

    public byte[] getInsurancesZip() {
        log.info("getInsurances start");
        String apiPath = this.ivassBasePath + GET_INSURANCES_PATH;
        return ivassRestTemplate.getForObject(apiPath, byte[].class);
    }
}
