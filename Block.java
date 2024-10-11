package main;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.security.MessageDigest;
import java.net.Socket;
import java.net.ServerSocket;

public class Block {
    private String data;
    private long timestamp;
    private int nonce;
    private String hash;
    private String previousHash;

    public Block(String data) {
        // Constructor implementation
    	this.data = data;
    	this.timestamp = System.currentTimeMillis();
    	this.hash = calculateBlockHash();
//    	this.previousHash()
//    	this.nonce = setNonce(this.hash);
    }

    public Block() {
        // Default constructor for Genesis Block
    }

    public String calculateBlockHash() {
        // Implementation
    }

    public String getHash() {
        // Implementation
    }

    public void setHash(String hash) {
        // Implementation
    }

    public String getPreviousHash() {
        // Implementation
    }

    public void setPreviousHash(String previousHash) {
        // Implementation
    }

    public int getNonce() {
        // Implementation
    }

    public void setNonce(int nonce) {
        // Implementation
    }

    @Override
    public String toString() {
        // Implementation
    }
    
    

    public static void main(String[] args) {
        // Main method implementation as provided in the original instructions
    }
    
    
}