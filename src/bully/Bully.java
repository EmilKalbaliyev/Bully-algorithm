package bully;

public class Bully {

   
    public static void election(int sourceID) throws InterruptedException {
    	System.out.println("\nStart election." ); 
        Server.leader = true;
        for (int i = Node.pid + 1; i <= Node.numberOfPorts; i++) {
        	Message message = new Message(sourceID, i, "Election");
            System.out.println("Send "+i+": "+message.message+" message." ); 
            Client.sendMessage(message);
            Thread.sleep(300);
            if(!Server.leader) {
                Thread.sleep(1700);
                break;
            }
        }

        if (Server.leader) {
        	Node.lid = sourceID;
            System.out.println("\nME    : I am the Leader.");
            for (int i = 1; i <= Node.numberOfPorts; i++) {
            	if(Node.pid != i){
            		Message mesg = new Message(sourceID, i, "Leader");
                	System.out.println("Send "+mesg.destinationID+": " + mesg.message+ " message." );
    	            Client.sendMessage(mesg);
    	        }
            }
        } else {
        	
        	checkLeader check = new checkLeader();         
	        Thread clientThread = new Thread(check);
	        clientThread.start();        
	        }
    }
}
