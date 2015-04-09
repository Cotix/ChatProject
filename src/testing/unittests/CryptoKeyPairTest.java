package testing.unittests;

import client.security.CryptoKeyPair;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by cotix on 4/9/15.
 */
public class CryptoKeyPairTest implements UnitTest {


    @Override
    public String test() {
        byte[] data;
        try {
            data = "testTESTEST!@#%$.\0 ok. \n\n\r\n\r\n\0 testtest.".getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            return "No ASCII encoding";
        }
        CryptoKeyPair keyA = new CryptoKeyPair();
        CryptoKeyPair keyB = new CryptoKeyPair();
        CryptoKeyPair keyAPublic = new CryptoKeyPair(keyA.getPublicKey());
        if (!Arrays.equals(keyA.decrypt(keyAPublic.encrypt(data)), data)) {
            return "Encrypting with public than decrypting breaks.";
        }
        if (!keyAPublic.verify(data, keyA.sign(data))) {
            return "Verifying of signed data gives false!";
        }
        if (keyAPublic.verify(data, keyB.sign(data))) {
            return "Verifying returns true even though its a different key!";
        }

        return null;
    }
}
