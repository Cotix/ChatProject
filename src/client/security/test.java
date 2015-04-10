package client.security;

import java.util.Arrays;

/**
 * Created by martijn on 9-4-15.
 */
public class test {

    public static void main(String[] args) {
        RSAEncryption rsaEncryption = new RSAEncryption();
        rsaEncryption.generateKeys();
        MessageSignature msgSig = new MessageSignature();
        String text1 = "Spannend1";
        String text2 = "Spannend2";

        byte[] data = msgSig.sign(text1.getBytes(), rsaEncryption.getPrivateKey());
        System.out.println("Spannend1: " + Arrays.toString(data));
        boolean verified = msgSig.verify(text1.getBytes(), data, rsaEncryption.getPublicKey());
        System.out.println("Verified1: " + verified);

        byte[] data2 = msgSig.sign(text2.getBytes(), rsaEncryption.getPrivateKey());
        System.out.println("Spannend2: " + Arrays.toString(data2));
        boolean verified2 = msgSig.verify(text2.getBytes(), data2, rsaEncryption.getPublicKey());
        System.out.println("Verified2: " + verified2);

        boolean verified3 = msgSig.verify(text2.getBytes(), data, rsaEncryption.getPublicKey());
        System.out.println("Verified3: " + verified3);

    }

}
