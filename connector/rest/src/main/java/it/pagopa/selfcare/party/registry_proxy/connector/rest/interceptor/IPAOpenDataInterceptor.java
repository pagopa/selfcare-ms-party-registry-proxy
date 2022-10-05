package it.pagopa.selfcare.party.registry_proxy.connector.rest.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IPAOpenDataInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.query("bom", "True");
    }

}
