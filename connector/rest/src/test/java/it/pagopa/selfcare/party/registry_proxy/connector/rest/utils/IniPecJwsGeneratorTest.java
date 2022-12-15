package it.pagopa.selfcare.party.registry_proxy.connector.rest.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.SSLData;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {IniPecJwsGenerator.class, String.class})
@ExtendWith(SpringExtension.class)
class IniPecJwsGeneratorTest {
    @Autowired
    private IniPecJwsGenerator iniPecJwsGenerator;

    @MockBean
    private IniPecSecretConfig iniPecSecretConfig;

    /**
     * Method under test: {@link IniPecJwsGenerator#createAuthRest()}
     */
    @Test
    void testCreateAuthRest() {
        SSLData sslData = new SSLData("Cert","Key", "Pub");
        when(iniPecSecretConfig.getIniPecAuthRestSecret()).thenReturn(sslData);
        assertThrows(RuntimeException.class, () -> iniPecJwsGenerator.createAuthRest());
        verify(iniPecSecretConfig).getIniPecAuthRestSecret();
    }
}

