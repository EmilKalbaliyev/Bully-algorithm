package bully;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 97L;

    public int sourceID;
    public int destinationID;
    public String message;

    public Message(int sourceID, int destinID, String message){
        this.sourceID = sourceID;
        this.destinationID = destinID;
        this.message = message;
    }
    
}
