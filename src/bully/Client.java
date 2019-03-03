package bully;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
                                            
public class Client implements Runnable{
	@Override
	public void run(){
		try {
			Bully.election(Node.pid);        
		} catch(InterruptedException ex) {
			System.out.println("Interrupted Exception");
		}
	}



    public static void sendMessage(Message response) {  
        Socket socket;
        ObjectOutputStream stream;

        try {
            socket = new Socket("localhost", Node.port[response.destinationID - 1]);

            stream = new ObjectOutputStream(socket.getOutputStream());
            stream.writeObject(response);
            stream.flush();
            stream.close();
            socket.close();
        } catch (IOException ex) {                                      
            System.out.println("Node " + response.destinationID + ": Crashed.");
        }
    }
}
