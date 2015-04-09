package testing.unittests;

import client.security.CryptoKeyPair;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

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
        CryptoKeyPair copyOfA  = new CryptoKeyPair(keyA.getRawPublicKey());
        CryptoKeyPair copyOfA2 = copyOfA;
        try {
            copyOfA2 = new CryptoKeyPair(keyA.toString());
        } catch (Base64DecodingException e) {
            e.printStackTrace();
        }
        if (!Arrays.equals(keyA.decrypt(copyOfA.encrypt(data)), data)) {
            return "Constructing a public key using raw public key not working";
        }
        if (!Arrays.equals(keyA.decrypt(copyOfA2.encrypt(data)), data)) {
            return "Constructing a public key using base64 public key not working";
        }

        return null;
    }
}
