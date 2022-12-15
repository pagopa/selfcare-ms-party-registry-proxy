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

    public IniPecSecretConfig(@Value("${inipec.secret.public-key}") String iniPecPublicKey,
                              @Value("${inipec.secret.private-key}") String iniPecPrivateKey,
                              @Value("${inipec.secret.certificate}") String iniPecCertificate) {
        log.info("founded secret value for secrets: {} {} {}", iniPecPublicKey, iniPecPrivateKey, iniPecCertificate);
        this.iniPecAuthRestSecret = new SSLData(iniPecCertificate, iniPecPrivateKey, iniPecPublicKey);
    }

}
