package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BCnode {
    private ArrayList<Block> blockchain;
    private int myPort;
    private List<Socket> connectedNodes;
    private ServerSocket serverSocket;
    private static final int MINING_DIFFICULTY = 5;
    private static String host = "localhost";
    public BCnode(int myPort, List<Integer> remotePorts) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        this.myPort = myPort;
        this.connectedNodes = new ArrayList<>();
        this.blockchain = new ArrayList<>();
        initializeBlockchain(remotePorts);
    }

    public void initializeBlockchain(List<Integer> remoteNodes) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Block genesisBlock = new Block("Genesis Block");
        addBlock(genesisBlock);

        try {
            serverSocket = new ServerSocket(myPort);
            new Thread(new ConnectionHandler(serverSocket, this)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Integer remoteNode : remoteNodes) {
        	String host = BCnode.host;
            int remotePort = myPort+1;
            connectToNode(host, remotePort);
        }

        if (!remoteNodes.isEmpty()) {
            obtainBlockchain();
        }
    }
    
    public void connectToNode(String host, int port) {
    	try {
    		Socket socket = new Socket(host,port);
    		connectedNodes.add(socket);
    		new Thread(new ReadHandler(socket,this)).start();
    		
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private void obtainBlockchain() throws NoSuchAlgorithmException {
        if (!connectedNodes.isEmpty()) {
            try {
                Socket socket = connectedNodes.get(0);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject("GET_BLOCKCHAIN");
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ArrayList<Block> receivedChain = (ArrayList<Block>) in.readObject();
                if (validateChain()) {
                    blockchain = receivedChain;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    
 
    public void addBlock(Block b) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    	
    	System.out.println("HI");
    	if (blockchain.size() > 1) {
    		System.out.println("Blockchain size is greater than 1");
	    	Block lastBlock= blockchain.get(blockchain.size()-1);
//	    	b.setPreviousHash(lastBlock.getPreviousHash());
	    	mineBlock(b);
	    	if(isValidNewBlock(b, lastBlock)) {//ngl idk about this
	    		blockchain.add(b);
	    		System.out.println("Block added to the chain. ");
	    	}
	    	else {System.out.println("Invalid block. Was NOT added to the chain");}	
    	}
    	else {
    		System.out.println("blockchain size is less than 1 (handling root)");
//    		b.setPreviousHash(null);
    		mineBlock(b);
    		blockchain.add(b);
    	
    		System.out.println("Root block was added to the chain");
    		
    	}
    	
    	
    	
    	
    	
    }
    private boolean isValidNewBlock(Block newB, Block pB) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
    	if(!pB.getHash().equals(newB.getPreviousHash())) {
			//checks if the previous has is being correlty refrenced to
			return false;
		}
		
		if(!newB.getHash().equals(newB.calculateBlockHash())) {
			//recalculates the new blocks hash to make sure the stored hash matches
			return false;
		}
		
		
		if(!newB.getHash().substring(MINING_DIFFICULTY).equals(new String(new char[MINING_DIFFICULTY]).replace('\0', '0'))) {
			//proof of work: makes sure this block has been mined(this is code he gave us idk how it works tbh)
			
			return false;
			
		}
		return true;
    	
    }
    private void mineBlock(Block b) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println("mining block..");
        // infinite loop
        String prefixZeros = new String(new char[MINING_DIFFICULTY]).replace('\0', '0');
        
        long startTime = System.currentTimeMillis();
        while (!b.getHash().substring(0, MINING_DIFFICULTY).equals(prefixZeros)) {
            if (System.currentTimeMillis() - startTime > 30000) { // 30 seconds timeout
            System.out.println("Mining timeout: Block could not be mined within 30 seconds.");
            return;
            }
            b.setNonce(b.getNonce() + 1);
            String newHash = b.calculateBlockHash();
            b.setHash(newHash);
        }
        System.out.println("Block mined: " + b.getHash());
    }

    private boolean validateChain() throws NoSuchAlgorithmException, UnsupportedEncodingException {
    	
    	// ensure for each, that Node previoushhash == hash[i-1]'s hash
//    	if(blockchain.get(0).getPreviousHash() == null) {
//    		return true; // root is always valid
//    	}
    
    	//this method is to check if the entier chain is valid "When validating, we might as well validate the entire chain just in case something got corrupted. "
    	//so this just goes through and checks one by one if the chain is valid by
    	//The block has the correct hash stored in it (by computing the hash again and checking it against the stored number).
    	//The block has the correct previousHash (by checking it against the hash stored in the previous block).
    	//The block's hash meets the PoW condition (by checking the prefixZeros).
    	for(int i =1; i< blockchain.size(); i++) {
    		System.out.println("validating chain..");
    		Block current= blockchain.get(i);
    		Block prev= blockchain.get(i-1);
    		if(!isValidNewBlock(current, prev)) {
    			return false;
    		}
    		
    	}
    	return true;
    }
    @Override
    public String toString() {
    	String result = "_";
        // Implementation
    	return result;
    }
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException { // given from project
        Scanner keyScan = new Scanner(System.in);
        // Grab my port number on which to start this node
        System.out.print("Enter port to start (on current IP): ");
        int myPort = keyScan.nextInt();
        
        // Need to get what other Nodes to connect to
        System.out.print("Enter remote ports (current IP is assumed): ");
        keyScan.nextLine(); // skip the NL at the end of the previous scan int
        String line = keyScan.nextLine();
        List<Integer> remotePorts = new ArrayList<Integer>();
        if (line != "") {
            String[] splitLine = line.split(" ");
            for (int i=0; i<splitLine.length; i++) {
                remotePorts.add(Integer.parseInt(splitLine[i]));
            }
        }
        // Create the Node
        BCnode n = new BCnode(myPort, remotePorts);
        
        String ip = "";
        try {
             ip = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        System.out.println("Node started on " + ip + ": " + myPort);
        
        // Node command line interface
        while(true) {
            System.out.println("\nNODE on port: " + myPort);
            System.out.println("1. Display Node's blockchain");
            System.out.println("2. Create/mine new Block");
            System.out.println("3. Kill Node");
            System.out.print("Enter option: ");
            int in = keyScan.nextInt();
            
            if (in == 1) {
                System.out.println(n);
                
            } else if (in == 2) {
                // Grab the information to put in the block
                System.out.print("Enter information for new Block: ");
                String blockInfo = keyScan.next();
                Block b = new Block(blockInfo);
                n.addBlock(b);
                
            } else if (in == 3) {
                // Take down the whole virtual machine (and all the threads)
                //   for this Node.  If we just let main end, it would leave
                //   up the Threads the node created.
                keyScan.close();
                System.exit(0);
            }
        }
    }

 
}
