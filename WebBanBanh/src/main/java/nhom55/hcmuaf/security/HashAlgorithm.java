package nhom55.hcmuaf.security;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashAlgorithm {
	public static String MD5 = "MD5";
	public static String SHA1 = "SHA-1";
	public static String SHA224 = "SHA-224";
	public static String SHA256 = "SHA-256";
	public static String SHA384 = "SHA-384";
	public static String SHA512 = "SHA-512";
	public static String SHA512_224 = "SHA-512/224";
	public static String SHA512_256 = "SHA-512/256";

	public static String hashText(String input, String algorithm) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		byte[] data = messageDigest.digest(input.getBytes());
		BigInteger bigInteger = new BigInteger(1, data);
		return bigInteger.toString(16);
	}

	public static String hashFile(String srcFile, String algorithm) throws NoSuchAlgorithmException, IOException {
		MessageDigest digest = MessageDigest.getInstance(algorithm);
		BufferedInputStream ips = new BufferedInputStream(new FileInputStream(srcFile));
		DigestInputStream dIps = new DigestInputStream(ips, digest);
		byte[] read = new byte[1024];
		while ((dIps.read(read)) > -1)
			;
		BigInteger bigInteger = new BigInteger(1, dIps.getMessageDigest().digest());
		return bigInteger.toString(16);
	}

}
