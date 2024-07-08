package it.pagopa.selfcare.party.registry_proxy.connector.rest.service;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.encoders.Base64;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public class KeyGenerator {
    private KeyGenerator() {}
    public static RSAPrivateKey getPrivateKey(String privateKey) throws IOException {
        PEMParser pemParser = new PEMParser(new StringReader(privateKey));
        Security.addProvider(new BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        Object object = pemParser.readObject();
        return (RSAPrivateKey) converter.getKeyPair((PEMKeyPair) object).getPrivate();
    }

    public static RSAPublicKey getPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        key = key.replace("-----BEGIN PUBLIC KEY-----\r\n", "").replace("-----END PUBLIC KEY-----", "");
        key = key.replaceAll("\\s+","");
        byte[] keyBytes = Base64.decode(key.getBytes(StandardCharsets.UTF_8));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
    }
}
