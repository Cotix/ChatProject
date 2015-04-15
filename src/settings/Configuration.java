package settings;


/**
 * The configuration class for the node.
 * This makes it easier to change the settings.
 */

public class Configuration {
    public static final String HOST = "localhost";
    public static final short PORT = 2001;
    public static final int BUFFER_SIZE = 500;
    public static boolean ONE_NODE_PER_IP = true;
    public static int ANNOUNCE_TIME = 3000;
    public static int MAX_PACKET_SIZE = 1024*1024; // Must be smaller than Integer.MAX_SIZE
    public static String ENCRYPTION_SCHEME = "RSA";
    public static String SYMMETRIC_SCHEME = "AES";
    public static int KEY_LENGTH = 1024;
    public static int ENCODED_KEY_LENGTH = 162;
    public static int SYMMETRIC_KEY_LENGTH = 128;
    public static String SIGN_INSTANCE = "SHA256withRSA";
    public static int SIGN_LENGTH = 128; //Has to be determined
    public static String RANDOM_SOURCE = "SHA1PRNG";
    public static final String NICKNAME = "Noah";
    public static int ADHOC_IDENTIFIER_LENGTH = 8;
    public static long TIMEOUT = 5000;
}
