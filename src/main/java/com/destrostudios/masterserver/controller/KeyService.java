package com.destrostudios.masterserver.controller;

import org.bouncycastle.openssl.PEMParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class KeyService {

    KeyPair readKeyPair(String algorithm, String filePathPrivateKey, String filePathPublicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            PrivateKey privateKey = readPrivateKey(filePathPrivateKey, keyFactory);
            PublicKey publicKey = readPublicKey(filePathPublicKey, keyFactory);
            return new KeyPair(publicKey, privateKey);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private PrivateKey readPrivateKey(String filePath, KeyFactory keyFactory) throws IOException, InvalidKeySpecException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) file.length()];
        dis.readFully(keyBytes);
        dis.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return keyFactory.generatePrivate(spec);
    }

    private PublicKey readPublicKey(String filePath, KeyFactory keyFactory) throws IOException, InvalidKeySpecException {
        PEMParser pemParser = new PEMParser(new FileReader(filePath));
        byte[] publicKeyBytes = pemParser.readPemObject().getContent();
        return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }
}
