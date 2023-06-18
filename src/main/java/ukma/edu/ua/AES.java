package ukma.edu.ua;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class AES {
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final int KEY_SIZE = 128;

    private static final SecretKey KEY;

    static {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(KEY_SIZE);
            KEY = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate AES key. " + e.getMessage());
        }
    }

    public static byte[] encrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, KEY);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt data. " + e.getMessage());
        }
    }

    public static byte[] decrypt(byte[] encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, KEY);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt data. " + e.getMessage());
        }
    }
}
