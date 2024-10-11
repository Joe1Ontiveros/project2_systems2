package main;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    public static void main(String[] args) { // given from project
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
