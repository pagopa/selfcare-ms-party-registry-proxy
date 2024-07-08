package it.pagopa.selfcare.party.registry_proxy.connector.rest.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import it.pagopa.selfcare.party.registry_proxy.connector.rest.model.JwtConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;

@Slf4j
public class AssertionGenerator {

    public String generateClientAssertion(JwtConfig jwtCfg, String privateKey) throws Exception {
        log.info("START - AssertionGenerator.generateClientAssertion");
        long startTime = System.currentTimeMillis();
        Algorithm alg = Algorithm.RSA256(KeyGenerator.getPrivateKey(privateKey));
        String jwtToken = JWT.create()
                .withSubject(jwtCfg.getSubject())
                .withIssuer(jwtCfg.getIssuer())
                .withAudience(jwtCfg.getAudience())
                .withKeyId(jwtCfg.getKid())
                .withClaim("purposeId", jwtCfg.getPurposeId())
                .withExpiresAt(new Date(System.currentTimeMillis() + 43200L))
                .withJWTId(UUID.randomUUID()
                        .toString())
                .withIssuedAt(new Date())
                .sign(alg);

        log.info("END - AssertionGenerator.generateClientAssertion Timelapse: {} ms", System.currentTimeMillis() - startTime);
        log.info("AssertionGenerator: {}", jwtToken);
        return jwtToken;
    }
}
