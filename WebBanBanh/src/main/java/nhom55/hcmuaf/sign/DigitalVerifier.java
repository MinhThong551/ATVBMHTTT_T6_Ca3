package nhom55.hcmuaf.sign;

import java.security.*;
import java.util.Base64;

public class DigitalVerifier {

    /**
     * Xác minh chữ ký điện tử cho dữ liệu.
     *
     * @param data      Dữ liệu gốc (dạng byte).
     * @param publicKey Public key dùng để xác minh.
     * @param signature Chữ ký điện tử (mã hóa Base64).
     * @return `true` nếu chữ ký hợp lệ, ngược lại `false`.
     * @throws NoSuchAlgorithmException Nếu thuật toán không được hỗ trợ.
     * @throws InvalidKeyException      Nếu public key không hợp lệ.
     * @throws SignatureException       Nếu có lỗi trong quá trình xác minh.
     */
    public static boolean verifySignature(byte[] data, PublicKey publicKey, String signature)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(data);
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return sig.verify(signatureBytes);
    }
}
