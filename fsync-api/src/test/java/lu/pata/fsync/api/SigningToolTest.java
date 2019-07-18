package lu.pata.fsync.api;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class SigningToolTest {
    @Test
    public void testSigning() {

    }

    private void test() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        FileInputStream is = new FileInputStream("keys.jceks");
        KeyStore keystore = KeyStore.getInstance("JCEKS");
        keystore.load(is, "salam".toCharArray());

        Key key = keystore.getKey("client a", "salam".toCharArray());

        java.security.cert.Certificate cert = keystore.getCertificate("client a");

        PublicKey publicKey = cert.getPublicKey();
        PrivateKey privateKey=(PrivateKey) key;

        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", "BC");
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update("CocoJambo".getBytes("UTF-8"));
        byte[] signature = ecdsaSign.sign();

        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", "BC");
        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update("CocoJambo".getBytes("UTF-8"));
        boolean result = ecdsaVerify.verify(signature);

        System.out.println(result);
    }
}