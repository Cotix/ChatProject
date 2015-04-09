package client.security;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import log.Log;
import log.LogLevel;
import settings.Configuration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;


public class CryptoKeyPair {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public CryptoKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(Configuration.ENCRYPTION_SCHEME);
            kpg.initialize(Configuration.KEY_LENGTH);
            KeyPair kp = kpg.genKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public CryptoKeyPair(PublicKey pub) {
        publicKey = pub;
        privateKey = null;
    }

    public CryptoKeyPair(String pub) throws Base64DecodingException {
        this(Base64.decode(pub));
    }

    public CryptoKeyPair(byte[] pub) {
        this(bytesToKey(pub));
    }

    public static PublicKey bytesToKey(byte[] key) {
        KeyFactory keyFactory;
        PublicKey publicKey2 = null;
        try {
            keyFactory = KeyFactory.getInstance(Configuration.ENCRYPTION_SCHEME);
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(key);
            publicKey2 = keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey2;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] encrypt(byte[] data) {
        byte[] cipherData;
        try {
            Cipher cipher = Cipher.getInstance(Configuration.ENCRYPTION_SCHEME);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipherData = cipher.doFinal(data);
        } catch (InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            Log.log("Encryption failed! Public key:" + publicKey.toString(), LogLevel.ERROR);
            cipherData = new byte[0];
        }
        return cipherData;
    }

    public byte[] decrypt(byte[] data) {
        if (privateKey == null) {
            Log.log("Can not decrypt without privatekey! Only have the publickey(" + publicKey.toString() + ")", LogLevel.ERROR);
        }
        byte[] cipherData;
        try {
            Cipher cipher = Cipher.getInstance(Configuration.ENCRYPTION_SCHEME);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            cipherData = cipher.doFinal(data);
        } catch (InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            Log.log("Encryption failed! Public key:" + publicKey.toString(), LogLevel.ERROR);
            return new byte[0];
        }
        return cipherData;
    }

    public boolean verify(byte[] data, byte[] signedBytes) {
        try {
            Signature signature = Signature.getInstance(Configuration.SIGN_INSTANCE);
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(signedBytes);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            Log.log("Verifying failed! Public key:" + publicKey.toString(), LogLevel.ERROR);
        }
        return false;
    }

    public byte[] sign(byte[] data) {
        if (privateKey == null) {
            Log.log("Can not sign without privatekey! Only have the publickey(" + publicKey.toString() + ")", LogLevel.ERROR);
        }
        byte[] res;
        try {
            Signature signature = Signature.getInstance(Configuration.SIGN_INSTANCE);
            signature.initSign(privateKey);
            signature.update(data);
            res = signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            Log.log("Signing failed! Public key:" + publicKey.toString(), LogLevel.ERROR);
            return new byte[0];
        }
        return res;
    }

    public byte[] getRawPublicKey() {
        return publicKey.getEncoded();
    }

    public String toString() {
        return Base64.encode(publicKey.getEncoded());
    }
}
