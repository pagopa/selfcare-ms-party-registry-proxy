package it.pagopa.selfcare.party.registry_proxy.connector.rest.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.pagopa.selfcare.party.registry_proxy.connector.exception.InternalException;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.JwtConfig;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.interfaces.RSAPublicKey;

import static org.junit.jupiter.api.Assertions.*;

class AssertionGeneratorTest {
    private final AssertionGenerator assertionGenerator = new AssertionGenerator();
    private String privateKey;

    @Test
    void generateClientAssertionSuccess() throws Exception {
        privateKey = new String(Files.readAllBytes(Paths.get("src/test/resources/private_key.txt")));
        String publicKey = new String(Files.readAllBytes(Paths.get("src/test/resources/public_key.txt")));
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setIssuer("Issuer");
        jwtConfig.setSubject("Subject");
        jwtConfig.setAudience("Audience");
        jwtConfig.setKid("Kid");
        jwtConfig.setPurposeId("PurposeId");
        String jwt = assertionGenerator.generateClientAssertion(jwtConfig, privateKey);
        DecodedJWT decodecJwt = JWT.decode(jwt);
        assertEquals("RS256", decodecJwt.getAlgorithm());
        assertEquals("Issuer", decodecJwt.getIssuer());
        assertEquals("Subject", decodecJwt.getSubject());
        assertEquals("Audience", decodecJwt.getAudience().get(0));
        assertEquals("Kid", decodecJwt.getKeyId());
        assertEquals("PurposeId", decodecJwt.getClaim("purposeId").asString());
        assertTrue(decodecJwt.getExpiresAt().getTime() > System.currentTimeMillis());
        assertTrue(decodecJwt.getIssuedAt().getTime() < System.currentTimeMillis());
        RSAPublicKey pubKey = KeyGenerator.getPublicKey(publicKey);
        final Algorithm algorithm = Algorithm.RSA256(pubKey, null);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        assertDoesNotThrow(() -> verifier.verify(jwt));
    }

    @Test
    void generateClientAssertionError() {
        privateKey = "testtesttesttesttesttesttesttesttest";
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setIssuer("Issuer");
        jwtConfig.setSubject("Subject");
        jwtConfig.setAudience("Audience");
        jwtConfig.setKid("Kid");
        jwtConfig.setPurposeId("PurposeId");
        assertThrows(InternalException.class, () -> assertionGenerator.generateClientAssertion(jwtConfig, privateKey));
    }
}