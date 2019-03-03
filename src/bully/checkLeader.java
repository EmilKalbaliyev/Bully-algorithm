package bully;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class checkLeader implements Runnable{
	@Override
	public void run(){
			checkLeader() ;        
	}
    public static void checkLeader() {   

	Socket socket;
    ObjectOutputStream stream;
    Message message = new Message(Node.pid, Node.lid, "Are you alive? ");
	System.out.println("Send "+message.destinationID+": " + message.message );
    try {
        socket = new Socket("localhost", Node.port[Node.lid - 1]); 
        stream = new ObjectOutputStream(socket.getOutputStream());
        stream.writeObject(message);
        stream.flush();
        stream.close();
        socket.close();
    } catch (IOException ex) {
        try {
			System.out.println("\nLeader: Crashed."); 
			//Thread.sleep(3000);
           Bully.election(message.sourceID);  
        } catch (InterruptedException e) {
        	System.out.println("Interrupted Exception");
        }
    }
    }
}
