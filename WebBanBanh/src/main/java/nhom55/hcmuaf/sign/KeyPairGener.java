package nhom55.hcmuaf.sign;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class KeyPairGener {

    /**
     * Tạo public key từ cặp khóa RSA.
     *
     * @return Chuỗi public key được mã hóa Base64.
     * @throws NoSuchAlgorithmException Nếu thuật toán RSA không được hỗ trợ.
     */
    public static String generatePublicKey() throws NoSuchAlgorithmException {
        KeyPair keyPair = generateKeyPairInternal();
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    /**
     * Tạo private key từ cặp khóa RSA.
     *
     * @return Chuỗi private key được mã hóa Base64.
     * @throws NoSuchAlgorithmException Nếu thuật toán RSA không được hỗ trợ.
     */
    public static String generatePrivateKey() throws NoSuchAlgorithmException {
        KeyPair keyPair = generateKeyPairInternal();
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    /**
     * Tạo một cặp khóa RSA nội bộ.
     *
     * @return Cặp khóa RSA.
     * @throws NoSuchAlgorithmException Nếu thuật toán RSA không được hỗ trợ.
     */
    private static KeyPair generateKeyPairInternal() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }
}
