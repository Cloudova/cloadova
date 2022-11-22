package com.cloudova.service.jwt.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class RSAKeyService {

    private static KeyPair keyPair;
    private final Logger logger = LoggerFactory.getLogger(RSAKeyService.class);

    public KeyPair getKeyPair() {
        if (keyPair == null) {
            try {
                this.generateKeyPair();
            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        return RSAKeyService.keyPair;
    }

    public PublicKey getPublicKey() {
        if (keyPair == null) {
            try {
                this.generateKeyPair();
            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        return RSAKeyService.keyPair.getPublic();
    }

    public void generateKeyPair() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        logger.info("################# Starting Authentication Service #################");

        File certificateFile = new File("certificate");
        if (!certificateFile.exists() || !(new File(certificateFile, "key.pub")).exists() || !(new File(certificateFile, "key").exists())) {
            this.generateKeyPair(certificateFile);
        } else {
            File privateKeyFile = new File(certificateFile, "key");
            byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            File publicKeyFile = new File(certificateFile, "key.pub");
            byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            RSAKeyService.keyPair = new KeyPair(publicKey, privateKey);
        }
        logger.info("RSA Key Pair Loaded");
    }

    private void generateKeyPair(File certificateFile) throws NoSuchAlgorithmException, IOException {
        //noinspection ResultOfMethodCallIgnored
        certificateFile.delete();

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = kpg.generateKeyPair();

        //noinspection ResultOfMethodCallIgnored
        certificateFile.mkdir();

        FileOutputStream publicKeyWriter = new FileOutputStream(new File(certificateFile, "key.pub"));
        publicKeyWriter.write(keyPair.getPublic().getEncoded());
        FileOutputStream privateKeyWriter = new FileOutputStream(new File(certificateFile, "key"));
        privateKeyWriter.write(keyPair.getPrivate().getEncoded());

        RSAKeyService.keyPair = keyPair;
    }

}
