import java.security.PublicKey;
import java.security.PrivateKey;

public class Transaction {
    public String transactionId; // Unique ID for the transaction
    public PublicKey sender;      // Public key of the sender
    public PublicKey recipient;   // Public key of the recipient
    public float value;           // Amount being transferred
    private byte[] signature;     // Transaction signature

    // Constructor
    public Transaction(PublicKey from, PublicKey to, float value) {
        this.sender = from;          // Initialize sender
        this.recipient = to;         // Initialize recipient
        this.value = value;          // Initialize value
        this.transactionId = calculateHash(); // Calculate the transaction ID
    }

    // Method to calculate the transaction ID
    private String calculateHash() {
        return StringUtil.applySha256(StringUtil.getStringFromKey(sender) +
                                       StringUtil.getStringFromKey(recipient) +
                                       Float.toString(value));
    }

    // Method to sign the transaction
    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + 
                      StringUtil.getStringFromKey(recipient) + 
                      Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data); // Sign the data
    }

    // Method to verify the transaction's signature
    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + 
                      StringUtil.getStringFromKey(recipient) + 
                      Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature); // Check signature validity
    }
}
