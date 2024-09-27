package com.example.documentapi.utils;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.file.*;
import java.security.spec.KeySpec;
public class Utils {

    public static byte[] encryptFile(byte[] fileContent, String password) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), "salt".getBytes(), 65536, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(fileContent);
    }

    public static byte[] decryptFile(byte[] fileContent, String password) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), "salt".getBytes(), 65536, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(fileContent);
    }
}

