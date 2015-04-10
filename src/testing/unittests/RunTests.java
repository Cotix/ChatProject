package testing.unittests;

import log.Log;

/**
 * Created by cotix on 4/9/15.
 */
public class RunTests {
    public static void main(String[] arg) {
        Log.enableAllLevels();
        CryptoKeyPairTest cryptoTest = new CryptoKeyPairTest();
        String res = cryptoTest.test();
        if (res != null) {
            System.out.println(res);
        }

        MessageConversionTest msgTest = new MessageConversionTest();
        res = msgTest.test();
        if (res != null) {
            System.out.println(res);
        }

    }
}
