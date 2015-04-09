package client.security;

import java.security.*;

/**
 * Created by martijn on 8-4-15.
 */
public class MessageSignature {

    private static final String INSTANCE = "SHA256withRSA";
    private static final int KEY_LENGTH = 1024;
    private static PrivateKey priv;
    private static PublicKey pub;

    public static void generateKeys() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(INSTANCE);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            kpg.initialize(KEY_LENGTH, random);
            KeyPair pair = kpg.generateKeyPair();
            priv = pair.getPrivate();
            pub = pair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static boolean verify(byte[] data, byte[] signedBytes, PublicKey publicKey) {
        boolean verified = false;
        try {
            Signature signature = Signature.getInstance(INSTANCE);
            signature.initVerify(publicKey);
            signature.update(data);
            verified = signature.verify(signedBytes);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return verified;
    }

    public static byte[] sign(byte[] data, PrivateKey privateKey) {
        byte[] res = null;
        try {
            Signature signature = Signature.getInstance(INSTANCE);
            signature.initSign(privateKey);
            signature.update(data);
            res = signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return res;
    }

}
