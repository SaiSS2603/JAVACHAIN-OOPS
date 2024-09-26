import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
    public PrivateKey privateKey; // Private key for signing transactions
    public PublicKey publicKey;    // Public key for receiving transactions

    // Constructor to create a new wallet with a key pair
    public Wallet() {
        generateKeyPair(); // Generate a new key pair
    }

    // Method to generate a public/private key pair
    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC"); // Use ECDSA algorithm
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG"); // Secure random number generator
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1"); // Specify the curve

            keyGen.initialize(ecSpec, random); // Initialize the key generator
            KeyPair keyPair = keyGen.generateKeyPair(); // Generate the key pair

            privateKey = keyPair.getPrivate(); // Get the private key
            publicKey = keyPair.getPublic(); // Get the public key
        } catch (Exception e) {
            throw new RuntimeException(e); // Handle exceptions
        }
    }
}
