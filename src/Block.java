import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hash;                   // Block hash
    public String previousHash;            // Hash of the previous block
    public long timeStamp;                 // Timestamp of block creation
    public int nonce;                      // Nonce used for mining
    public ArrayList<Transaction> transactions; // List of transactions in the block

    // Constructor
    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.transactions = new ArrayList<Transaction>(); // Initialize the transaction list
        this.hash = calculateHash(); // Calculate the block's hash
    }

    // Method to calculate the hash of the block
    public String calculateHash() {
        String calculatedHash = StringUtil.applySha256(
                previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + transactions.toString()
        );
        return calculatedHash;
    }

    // Method to add a transaction to the block
    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) return false;
        if (!transaction.verifySignature()) {
            System.out.println("Transaction Signature failed to verify");
            return false;
        }
        transactions.add(transaction); // Add the valid transaction to the block
        return true;
    }

    // Method to mine the block
    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0'); // Create target string
        while (!hash.substring(0, difficulty).equals(target)) { // Check if hash meets difficulty target
            nonce++; // Increment nonce
            hash = calculateHash(); // Recalculate hash
        }
        System.out.println("Block Mined!!! : " + hash); // Output mined block's hash
    }
}
