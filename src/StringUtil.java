import java.security.*;
import java.util.Base64;

public class StringUtil {
    // Applies SHA-256 to a string and returns the result.
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Convert PublicKey to String
    public static String getStringFromKey(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    // Apply ECDSA signature and return the signature
    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] signature = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC"); // Using Bouncy Castle provider
            dsa.initSign(privateKey);
            dsa.update(input.getBytes());
            signature = dsa.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return signature;
    }

    // Verify the ECDSA signature
    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
