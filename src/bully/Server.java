package bully;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server implements Runnable {

    private Socket client = null;
    public static boolean leader = false;

    public Server() {
        server();
    }

    public Server(Socket client) {
        this.client = client;
    }

    public static void server() {

        try {
            ServerSocket serverSocket = new ServerSocket(Node.port[Node.pid - 1]);
            while (true) {

                Socket client = serverSocket.accept();

                Server server = new Server(client);
                Thread t = new Thread(server);

                t.start();
            }
        } catch (IOException ex) {
            System.out.println("IO Exception");
        }
    }

    public static void responseFromLeader(Message message) {
    	if ("Election".equals(message.message)) {            
            System.out.println("\nNode  "+message.sourceID +" : Election started"); 
            if (message.destinationID > message.sourceID) {
                Message resp = new Message(message.destinationID, message.sourceID, "OK");  
                System.out.println("Send " + message.sourceID +": "+resp.message); 
                Client.sendMessage(resp);
                try {	   
	                Bully.election(message.destinationID); 
	                
	            } catch (InterruptedException ex) {
	            	System.out.println("Interrupted Exception");
	            }
            } else {
            	try {
            		leader = false;
            		Thread.sleep(1500);
            	} catch (InterruptedException ex) {
	            	System.out.println("Interrupted Exception");
            	}
            }
        } else if ("Leader".equals(message.message)) {        
        	Node.lid = message.sourceID;
        	System.out.println("\nNode "+message.sourceID +": is the leader now"  );
        	System.out.println( "ME     : Node "+message.sourceID +"is the new leader  " );
        	System.out.println( "ME     : Check the leader. \n " );
        	
        	checkLeader check = new checkLeader();         
	        Thread clientThread = new Thread(check);
	        clientThread.start();
	        } else {                                                 
		    
		    System.out.println("Node "+message.sourceID +": "+message.message);
		    Message response = new Message(Node.lid, message.sourceID, "Yes, I am alive ");
		    System.out.println("Send "+message.sourceID +": "+response.message);
		    Client.sendMessage(response);
        }
    }

    public static void response(Message message) throws InterruptedException { 
        if ("Election".equals(message.message)) {
           System.out.println("\nNode "+message.sourceID +": Election started"); 
            if (message.destinationID > message.sourceID) { 
                Message mesg = new Message(message.destinationID, message.sourceID, "OK");   
                System.out.println("Answ " + message.sourceID +": "+mesg.message); 
                Client.sendMessage(mesg);
                Bully.election(message.destinationID); 
            }
        } else if ("OK".equals(message.message)) {      
            System.out.println("\nNode "+ message.sourceID +": "+message.message  );  
            leader = false;
            Thread.sleep(2500);
        } else if ("Leader".equals(message.message)) {     
        	Node.lid = message.sourceID;                                                         
        	System.out.println("\nNode "+message.sourceID +": is the leader now"  );
        	System.out.println( "ME     : Node "+message.sourceID +"is the new leader  " );
        	System.out.println( "ME     : Check the leader. \n " );


        } else {                                     
            switch (message.destinationID) {
                case 1:
                    Thread.sleep(2000);
                    break;
                case 2:
                    Thread.sleep(2500);
                    break;
                case 3:
                    Thread.sleep(3500);
                    break;
                case 4:
                    Thread.sleep(1500);
                    break;
            }
		    System.out.println("Leader" +": "+message.message);

		    checkLeader check = new checkLeader();         
	        Thread clientThread = new Thread(check);
	        clientThread.start();
        }
    }

    @Override
    public void run() {
        try {
            try (ObjectInputStream stream = new ObjectInputStream(this.client.getInputStream())) {
                Message message = (Message) stream.readObject();
                if(message.sourceID != Node.pid) {
                    if (message.destinationID == Node.lid) {
                    	responseFromLeader(message);
                    } else {
                        try {
                        	response(message);
                        } catch (InterruptedException ex) {
        	            	System.out.println("Interrupted Exception");
                        }
                    }
                }
            }
            this.client.close();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Exception");
        }
    }
}
