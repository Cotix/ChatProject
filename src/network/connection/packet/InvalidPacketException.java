package network.connection.packet;

public class InvalidPacketException extends Exception {
    public InvalidPacketException() {
        super("Invalid packet header!");
    }
}
