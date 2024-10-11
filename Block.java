package main;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.Socket;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;

public class Block {
    private String data;
    private long timestamp;
    private int nonce;
    private String hash;
    private String previousHash;
    private List<String> hashhistory = new ArrayList<String>();
 
    public Block(String data) {
        // Constructor implementation
    	this.data = data;
    	this.timestamp = new Date().getTime();
    	try {
			this.hash = calculateBlockHash();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	this.previousHash()
    	setNonce(0);
    	
    }

    public Block() {
        // Default constructor for Genesis Block
    }

    public String calculateBlockHash() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // condense all vars to one large string
    	setPreviousHash(this.hash); // save old hash
    	String combined = this.data + this.timestamp + this.hash;
    	System.out.printf("Block combined string",combined);
    	MessageDigest mydigest = MessageDigest.getInstance("SHA-256");
    	
    	byte[] hashBytes = mydigest.digest(combined.getBytes("UTF-8"));
    	
    	StringBuffer buffer = new StringBuffer();
    	for (byte b: hashBytes) {
    	      buffer.append(String.format("%02x", b));
    	}
    	String hashStr = buffer.toString();
    	return hashStr;
    	
    	
    }

    public String getHash() {
        // Implementation
    	return this.hash;
    }

    public void setHash(String hash) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // Implementation
    	this.data = hash;
    	calculateBlockHash();
    }

    public String getPreviousHash() {
        // Implementation
//    	this.hashhistory.get(index);
    	return this.previousHash;
    }

    public void setPreviousHash(String previousHash) {
        // Implementation
    	this.previousHash = new String(previousHash);
    }

    public int getNonce() {
        // Implementation
    	return this.nonce;
    	
    }

    public void setNonce(int nonce) {
        // Implementation
    	this.nonce = nonce;
    }
    @Override
    public String toString() {
    	String outstr = "[Time: %d] Node Hash: %s with Nonce: %d ";
    	String result = String.format(outstr,this.timestamp,this.hash,this.nonce);
    	return result;
    }
      
}