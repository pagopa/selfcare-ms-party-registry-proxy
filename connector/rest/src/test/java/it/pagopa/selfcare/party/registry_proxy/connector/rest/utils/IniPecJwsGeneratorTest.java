package it.pagopa.selfcare.party.registry_proxy.connector.rest.utils;

import static org.mockito.Mockito.when;

import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.SSLData;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.spec.InvalidKeySpecException;

@ExtendWith(SpringExtension.class)
class IniPecJwsGeneratorTest {
    @InjectMocks
    private IniPecJwsGenerator iniPecJwsGenerator;

    @Mock
    private IniPecSecretConfig iniPecSecretConfig;

    @Test
    void testCreateAuthRest() {
        SSLData sslData = new SSLData("Cert","Key", "Pub");
        when(iniPecSecretConfig.getIniPecAuthRestSecret()).thenReturn(sslData);
        Assertions.assertThrows(RuntimeException.class,()->iniPecJwsGenerator.createAuthRest("scope"));
    }

    @Test
    void testgetPrivateKey() {
        IniPecJwsGenerator authRest = new IniPecJwsGenerator("secret1","",iniPecSecretConfig);
        Assertions.assertThrows(InvalidKeySpecException.class,()->authRest.getPrivateKey("dGVzdA=="));
    }
}

