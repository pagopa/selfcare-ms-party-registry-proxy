package it.pagopa.selfcare.party.registry_proxy.connector.rest.utils;

import com.auth0.jwt.HeaderParams;
import com.auth0.jwt.RegisteredClaims;
import com.auth0.jwt.algorithms.Algorithm;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.SSLData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Component
@Slf4j
public class IniPecJwsGenerator {
    private final String aud;
    private final IniPecSecretConfig iniPecSecretConfig;
    private final String clientId;

    public IniPecJwsGenerator(@Value("${inipec.aud}") String aud,
                              @Value("${inipec.client-id}") String clientId,
                              IniPecSecretConfig iniPecSecretConfig) {
        this.aud = aud;
        this.iniPecSecretConfig = iniPecSecretConfig;
        this.clientId = clientId;
    }

    public String createAuthRest() {
        try {
            log.info("start to createAuthRest");
            SSLData sslData = iniPecSecretConfig.getIniPecAuthRestSecret();
            return JWT.create().withHeader(createHeaderMap(sslData)).withPayload(createClaimMap())
                    .sign(Algorithm.RSA256(getPublicKey(sslData.getPub()), getPrivateKey(sslData.getKey())));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> createHeaderMap(SSLData sslData) {
        Map<String, Object> map = new HashMap<>();
        map.put(HeaderParams.TYPE, "JWT");
        map.put(HeaderParams.ALGORITHM, "ES256");
        map.put("x5c", List.of(sslData.getCert()));
        log.debug("HeaderMap type: {}, alg: {}",map.get(HeaderParams.TYPE), map.get(HeaderParams.ALGORITHM));
        return map;
    }

    private Map<String, Object> createClaimMap() {
        Map<String, Object> map = new HashMap<>();
        long nowSeconds = System.currentTimeMillis() / 1000L;
        long expireSeconds = nowSeconds + 60;

        map.put(RegisteredClaims.AUDIENCE, aud);
        map.put(RegisteredClaims.EXPIRES_AT, expireSeconds);
        map.put(RegisteredClaims.ISSUER, clientId);
        map.put(RegisteredClaims.SUBJECT, clientId);
        map.put(RegisteredClaims.JWT_ID, UUID.randomUUID().toString());

        log.debug("ClaimMap audience: {}",map.get(RegisteredClaims.AUDIENCE));
        return map;
    }

    protected RSAPublicKey getPublicKey(String pub) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        log.debug("start getPublicKey");
        InputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(pub));
        X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(is.readAllBytes());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(encodedKeySpec);
    }

    protected RSAPrivateKey getPrivateKey(String key) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        log.debug("start getPrivateKey");
        InputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(key));
        PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(is.readAllBytes());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(encodedKeySpec);
    }
}
