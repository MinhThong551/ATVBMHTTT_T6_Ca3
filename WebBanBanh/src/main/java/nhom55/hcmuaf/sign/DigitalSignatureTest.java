package nhom55.hcmuaf.sign;



import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class DigitalSignatureTest {

    public static void main(String[] args) {
        try {
            // Bước 1: Tạo cặp khóa RSA
            KeyPair keyPair = KeyPairGener.generateKeyPairInternal();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            System.out.println("Public Key: " + KeyPairGener.generatePublicKey());
            System.out.println("Private Key: " + KeyPairGener.generatePrivateKey());

            // Bước 2: Đọc file dữ liệu cần ký
            String filePath = "D:\\testMaHoaFile\\aaaaa.txt"; // Thay bằng đường dẫn file của bạn
            byte[] fileData = Files.readAllBytes(Paths.get(filePath));
            System.out.println("Dữ liệu file: " + new String(fileData));

            // Bước 3: Ký dữ liệu
            String signature = DigitalSigner.signData(fileData, privateKey);
            System.out.println("Chữ ký điện tử: " + signature);

            // Bước 4: Xác minh chữ ký
            boolean isSignatureValid = DigitalVerifier.verifySignature(fileData, publicKey, signature);
            if (isSignatureValid) {
                System.out.println("✅ Chữ ký hợp lệ!");
            } else {
                System.out.println("❌ Chữ ký không hợp lệ!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
