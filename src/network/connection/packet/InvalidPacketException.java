package network.connection.packet;

/**
 * Created by cotix on 4/6/15.
 */
public class InvalidPacketException extends Exception {
    public InvalidPacketException() {
        super("Invalid packet header!");
    }
}
