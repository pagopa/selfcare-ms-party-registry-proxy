package it.pagopa.selfcare.party.registry_proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "it.pagopa.selfcare.party.*")
public class SelfCarePartyRegistryProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SelfCarePartyRegistryProxyApplication.class, args);
    }

}
