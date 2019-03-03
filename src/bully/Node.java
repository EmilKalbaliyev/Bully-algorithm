package bully;

public class Node {

    public static int pid; //process id
    public static int lid; //leader id
    public static int numberOfPorts = 4;
    public static int[] port = {11111, 22222, 33333, 44444}; 

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args[0] != null) {
            pid = Integer.parseInt(args[0]);
        }

        Client client = new Client();         
        Thread clientThread = new Thread(client);
        clientThread.start();

        Server server = new Server();       
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}
