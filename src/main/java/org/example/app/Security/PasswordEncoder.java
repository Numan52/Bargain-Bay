package org.example.app.Security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordEncoder {

    public static String[] hashPassword(String password, String saltString) throws Exception {
        byte[] salt = null;

        if (saltString == null) {
            SecureRandom random = new SecureRandom();
            salt = new byte[16];
            random.nextBytes(salt);
        } else  {
            salt = Base64.getDecoder().decode(saltString);
        }


        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hash = factory.generateSecret(spec).getEncoded();
        return new String[] {Base64.getEncoder().encodeToString(salt), Base64.getEncoder().encodeToString(hash)};
    }
}
