package me.rqmses.aktiboom.utils;

import java.util.Base64;

public class EncryptionUtils {
    public static String encode(String string, int key) {
        string = enBase(string);
        string = enRot(string, key);
        string = enCycle(string, key % string.length());
        return string;
    }

    public static String decode(String string, int key) {
        string = deCycle(string, key % string.length());
        string = deRot(string, key);
        string = deBase(string);
        return string;
    }

    private static String enBase(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes());
    }

    private static String deBase(String string) {
        return new String(Base64.getDecoder().decode(string));
    }

    private static String enRot(String plaintext, int key) {
        StringBuilder ciphertext = new StringBuilder();
        String temptext = plaintext.toLowerCase();
        for (int i = 0; i < temptext.length(); i++) {
            char next = shift(temptext.charAt(i), key);
            if (Character.isUpperCase(plaintext.charAt(i))) next = Character.toUpperCase(next);
            ciphertext.append(next);
        }
        return ciphertext.toString();
    }

    private static String deRot(String ciphertext, int key) {
        StringBuilder decoded = new StringBuilder();
        String temptext = ciphertext.toLowerCase();
        for (int i = 0; i < temptext.length(); i++) {
            char next = shift(temptext.charAt(i), 26 - key);
            if (Character.isUpperCase(ciphertext.charAt(i))) next = Character.toUpperCase(next);
            decoded.append(next);
        }
        return decoded.toString();
    }

    private static char shift(char letter, int shift) {
        if (letter>='a' && letter <='z') {
            letter += shift;
            while (letter > 'z') {
                letter -= 26;
            }
        }
        return letter;
    }

    private static String enCycle(String string, int key) {
        for (int i = 0; i < key; i++) {
            string = string.charAt(string.length() - 1) + string.substring(0, string.length() - 1);
        }
        return string;
    }

    private static String deCycle(String string, int key) {
        for (int i = 0; i < string.length() - key; i++) {
            string = string.charAt(string.length() - 1) + string.substring(0, string.length() - 1);
        }
        return string;
    }
}
