package client.Encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

/**
 * Created by martijn on 8-4-15.
 */
public class RSAEncryption {

    private static final String RSA = "RSA";
    private static final int KEY_LENGTH = 1024;
    private Key publicKey;
    private Key privateKey;

    public RSAEncryption() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
            kpg.initialize(KEY_LENGTH);
            KeyPair kp = kpg.genKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public byte[] rsaEncrypt(byte[] data) {
        byte[] cipherData = null;
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipherData = cipher.doFinal(data);
        } catch (InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return cipherData;
    }

    public byte[] rsaDecrypt(byte[] data) {
        byte[] cipherData = null;
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            cipherData = cipher.doFinal(data);
        } catch (InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return cipherData;
    }

    public Key getPublicKey() {
        return publicKey;
    }

}
