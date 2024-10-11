package main;

import java.util.ArrayList;
import java.util.List;

public class BCnode {
    private ArrayList<Block> blockchain;
    private int myPort;
    private List<Integer> remotePorts;

    public BCnode(int myPort, List<Integer> remotePorts) {
    	this.myPort = myPort; // takes in port
        this.remotePorts = remotePorts; // remote ports also given 
        this.blockchain = new ArrayList<>();// array for the blockchain
        initializeBlockchain(); // start it 
    }

    public void initializeBlockchain() {
    	Block b1 = new Block("ABC");
    }
    
    public void addBlock(Block b) {
        // Implementation
    }

    private void mineBlock(Block b) {
        // Implementation
    }

    private boolean validateChain() {
    	boolean result = false;
        // Implementation
    	
    	
    	return result;
    }

    @Override
    public String toString() {
    	String result = "_";
        // Implementation
    	return result;
    }
}
