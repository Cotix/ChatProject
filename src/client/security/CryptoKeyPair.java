package client.security;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import log.Log;
import log.LogLevel;
import settings.Configuration;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;


public class CryptoKeyPair {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public CryptoKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(Configuration.ENCRYPTION_SCHEME);
            SecureRandom random = SecureRandom.getInstance(Configuration.RANDOM_SOURCE);
            kpg.initialize(Configuration.KEY_LENGTH, random);
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

    private SecretKey generateSymmetricKey() {
        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance(Configuration.SYMMETRIC_SCHEME);
        } catch (NoSuchAlgorithmException e) {
            Log.log("No encryption scheme " + Configuration.SYMMETRIC_SCHEME + " found!", LogLevel.ERROR);
            return null;
        }
        keyGen.init(Configuration.SYMMETRIC_KEY_LENGTH);
        return keyGen.generateKey();
    }

    private byte[] publicKeyEncrypt(byte[] data) {
        byte[] cipherData;
        try {
            Cipher cipher = Cipher.getInstance(Configuration.ENCRYPTION_SCHEME);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipherData = cipher.doFinal(data);
        } catch (InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            Log.log("Encryption failed! Reason: " + e.getMessage() + "  Public key:" + publicKey.toString(), LogLevel.ERROR);
            cipherData = new byte[0];
        }
        return cipherData;
    }

    private byte[] publicKeyDecrypt(byte[] data) {
        if (privateKey == null) {
            Log.log("Can not decrypt without private key! Only have the public key(" + publicKey.toString() + ")", LogLevel.ERROR);
        }
        byte[] cipherData;
        try {
            Cipher cipher = Cipher.getInstance(Configuration.ENCRYPTION_SCHEME);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            cipherData = cipher.doFinal(data);
        } catch (InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            Log.log("Encryption failed! Reason: " + e.getMessage() + "  Public key:" + publicKey.toString(), LogLevel.ERROR);
            cipherData = new byte[0];
        }
        return cipherData;
    }

    public byte[] encrypt(byte[] data) {
        SecretKey key = generateSymmetricKey();
        byte[] cryptedKey = publicKeyEncrypt(key.getEncoded());
        byte[] cipherData;
        try {
            Cipher cipher = Cipher.getInstance(Configuration.SYMMETRIC_SCHEME);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherData = cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            Log.log("No encryption scheme " + Configuration.SYMMETRIC_SCHEME + " found!", LogLevel.ERROR);
            return new byte[0];
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            Log.log("Encryption failed! Reason: " + e.getMessage() + "  Public key:" + publicKey.toString(), LogLevel.ERROR);
            cipherData = new byte[0];
        }
        byte[] res = new byte[cryptedKey.length + cipherData.length];
        System.arraycopy(cryptedKey, 0, res, 0, cryptedKey.length);
        System.arraycopy(cipherData, 0, res, cryptedKey.length, cipherData.length);
        return res;
    }

    public byte[] decrypt(byte[] data) {
        if (privateKey == null) {
            Log.log("Can not decrypt without private key! Only have the public key(" + publicKey.toString() + ")", LogLevel.ERROR);
        }
        byte[] key = new byte[Configuration.SYMMETRIC_KEY_LENGTH];
        byte[] d = new byte[data.length - key.length];
        System.arraycopy(data, 0, key, 0, key.length);
        System.arraycopy(data, key.length, d, 0, d.length);
        key = publicKeyDecrypt(key);
        SecretKey symKey = new SecretKeySpec(key, Configuration.SYMMETRIC_SCHEME);
        byte[] cipherData;
        try {
            Cipher cipher = Cipher.getInstance(Configuration.SYMMETRIC_SCHEME);
            cipher.init(Cipher.DECRYPT_MODE, symKey);
            cipherData = cipher.doFinal(d);
        } catch (IllegalBlockSizeException | InvalidKeyException
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
            Log.log("Can not sign without private key! Only have the public key(" + publicKey.toString() + ")", LogLevel.ERROR);
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
        return Base64.encode(publicKey.getEncoded()).replace("\n","");
    }

    public int hashCode() {
        byte[] data = publicKey.getEncoded();
        int res = 0;
        long multiplier = 65353;
        for (int i = 0; i <= data.length; ++i) {
            res += data[i] * multiplier;
            multiplier *= 23331 + 7331;
        }
        return res;
    }

    public boolean equals(Object k) {

        return Arrays.equals(((CryptoKeyPair)k).getRawPublicKey(), getRawPublicKey());
    }
}
