package com.pvelychko.utils;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class Crypto {
    private static StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

    public static String encrypt(final String password_plaintext) {
        return passwordEncryptor.encryptPassword(password_plaintext);
    }

    public static boolean checkPassword(final String password_plaintext, final String stored_hash) {
        return passwordEncryptor.checkPassword(password_plaintext, stored_hash);
    }
}
