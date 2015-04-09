package settings;

//A nice configuration file
//Can be used to easily change some settings

public class Configuration {
    public static boolean ONENODEPERIP = true;
    public static int ANNOUNCETIME = 3000;
    public static int MAXPACKETSIZE = 1024*1024; // Must be smaller than Integer.MAX_SIZE.
}
