package testing.unittests;

/**
 * Created by cotix on 4/9/15.
 */
public class RunTests {
    public static void main(String[] arg) {
        CryptoKeyPairTest cryptoTest = new CryptoKeyPairTest();
        String res = cryptoTest.test();
        if (res != null) {
            System.out.println(res);
        }
    }
}
