package com.example.myapplication.crypt;

import android.util.Base64;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Cryptor {
    private static final String AES = "AES";
    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    private static final byte[] KEY_SEED;

    static {
        String p1 = "X7k9#mP2";
        String p2 = "qR4$vN8*";
        String p3 = "wL1@zB6!";
        KEY_SEED = (p1 + p2 + p3).getBytes(StandardCharsets.UTF_8);
    }

    private static SecretKeySpec deriveKey() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = md.digest(KEY_SEED);
        return new SecretKeySpec(keyBytes, AES);
    }

    /** Build-time: encrypt plaintext to Base64(IV + ciphertext) */
    public static String encrypt(String plaintext) {
        try {
            SecretKeySpec key = deriveKey();
            byte[] iv = new byte[IV_LENGTH];
            SecureRandom.getInstanceStrong().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(AES_GCM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            ByteBuffer buf = ByteBuffer.allocate(iv.length + ciphertext.length);
            buf.put(iv);
            buf.put(ciphertext);
            return Base64.encodeToString(buf.array(), Base64.NO_WRAP);
        } catch (Exception e) {
            throw new RuntimeException("Encrypt failed", e);
        }
    }

    /** Runtime: decrypt Base64(IV + ciphertext) back to plaintext */
    public static String decrypt(String encrypted) {
        try {
            SecretKeySpec key = deriveKey();
            byte[] decoded = Base64.decode(encrypted, Base64.NO_WRAP);

            ByteBuffer buf = ByteBuffer.wrap(decoded);
            byte[] iv = new byte[IV_LENGTH];
            buf.get(iv);
            byte[] ciphertext = new byte[buf.remaining()];
            buf.get(ciphertext);

            Cipher cipher = Cipher.getInstance(AES_GCM);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] plaintext = cipher.doFinal(ciphertext);
            return new String(plaintext, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "[decrypt error]";
        }
    }

    /** CLI helper: java Cryptor.java encrypt <plaintext> */
    public static void main(String[] args) {
        if (args.length >= 2 && "encrypt".equals(args[0])) {
            System.out.println(encrypt(args[1]));
        } else {
            System.out.println("Usage: java Cryptor encrypt <plaintext>");
        }
    }
}
