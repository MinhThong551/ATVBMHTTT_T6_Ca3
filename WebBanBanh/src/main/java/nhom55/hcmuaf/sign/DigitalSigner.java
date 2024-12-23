package nhom55.hcmuaf.sign;

import java.security.*;
import java.util.Base64;

public class DigitalSigner {

    /**
     * Tạo chữ ký điện tử cho dữ liệu đầu vào.
     *
     * @param data       Dữ liệu cần ký (dạng byte).
     * @param privateKey Private key dùng để ký.
     * @return Chữ ký điện tử được mã hóa Base64.
     * @throws NoSuchAlgorithmException Nếu thuật toán không được hỗ trợ.
     * @throws InvalidKeyException      Nếu private key không hợp lệ.
     * @throws SignatureException       Nếu có lỗi trong quá trình tạo chữ ký.
     */
    public static String signData(byte[] data, PrivateKey privateKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        byte[] signedBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signedBytes);
    }
}
