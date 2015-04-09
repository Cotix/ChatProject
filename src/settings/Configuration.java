package settings;

//A nice configuration file
//Can be used to easily change some settings

public class Configuration {
    public static boolean ONE_NODE_PER_IP = true;
    public static int ANNOUNCE_TIME = 3000;
    public static int MAX_PACKET_SIZE = 1024*1024; // Must be smaller than Integer.MAX_SIZE
    public static String ENCRYPTION_SCHEME = "RSA";
    public static int KEY_LENGTH = 1024;
    public static String SIGN_INSTANCE = "SHA256withRSA";
}
