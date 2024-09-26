import com.google.gson.GsonBuilder;
import java.security.Security;
import java.util.ArrayList;
import org.bouncycastle.jce.provider.BouncyCastleProvider; // Add this import

public class JavaChain {
    public static ArrayList<Block> blockchain = new ArrayList<>(); // The blockchain
    public static int difficulty = 6; // Difficulty level for mining
    public static Wallet walletA; // Wallet for User A
    public static Wallet walletB; // Wallet for User B

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider()); // Register the provider

        // Create wallets for two users
        walletA = new Wallet(); // Create wallet A
        walletB = new Wallet(); // Create wallet B

        // Create the genesis block
        Block genesisBlock = new Block("0"); // Previous hash is 0
        System.out.println("Mining Genesis block...");
        genesisBlock.mineBlock(difficulty); // Mine the genesis block
        blockchain.add(genesisBlock); // Add it to the blockchain

        // Create a transaction from walletA to walletB
        Transaction transaction1 = new Transaction(walletA.publicKey, walletB.publicKey, 5); // Create transaction
        transaction1.generateSignature(walletA.privateKey); // Sign the transaction
        genesisBlock.addTransaction(transaction1); // Add the transaction to the genesis block

        // Mine the first block with the transaction
        Block block1 = new Block(blockchain.get(blockchain.size() - 1).hash); // Create new block
        System.out.println("Trying to mine Block 1...");
        block1.mineBlock(difficulty); // Mine the block
        blockchain.add(block1); // Add block to blockchain

        // Print out the blockchain
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("\nThe blockchain: ");
        System.out.println(blockchainJson);

        // Validate the blockchain
        System.out.println("Is blockchain valid? " + isChainValid());
    }

    // Method to validate the blockchain
    public static boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i); // Get the current block
            previousBlock = blockchain.get(i - 1); // Get the previous block

            // Check if current block's hash is correct
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }

            // Check if previous block's hash matches current block's previousHash
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous Hashes not equal");
                return false;
            }

            // Validate transactions in the block
            for (Transaction transaction : currentBlock.transactions) {
                if (!transaction.verifySignature()) {
                    System.out.println("Transaction signature is invalid");
                    return false;
                }
            }
        }
        return true; // If all checks passed, the blockchain is valid
    }
}
