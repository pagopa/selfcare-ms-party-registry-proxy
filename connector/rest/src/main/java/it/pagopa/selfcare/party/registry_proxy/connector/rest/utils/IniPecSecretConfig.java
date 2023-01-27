package it.pagopa.selfcare.party.registry_proxy.connector.rest.utils;

import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.SSLData;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class IniPecSecretConfig {
    private final SSLData iniPecAuthRestSecret;

    public IniPecSecretConfig(@Value("${rest-client.info-camere.public-key}") String iniPecPublicKey,
                              @Value("${rest-client.info-camere.private-key}") String iniPecPrivateKey,
                              @Value("${rest-client.info-camere.certificate}") String iniPecCertificate) {
        log.info("founded secret value for secrets: {} {} {}", iniPecPublicKey, iniPecPrivateKey, iniPecCertificate);
        this.iniPecAuthRestSecret = new SSLData(iniPecCertificate, iniPecPrivateKey, iniPecPublicKey);
    }

}
