package lu.pata.fsync.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class SigningTool{
    private String keyFilename;
    private char[] password;
    private String alias;

    public SigningTool(String keyFilename, char[] password, String alias) {
        this.keyFilename = keyFilename;
        this.password = password;
        this.alias = alias;
    }

    public byte[] sign(byte[] data) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, NoSuchProviderException, InvalidKeyException, SignatureException {
        FileInputStream is = new FileInputStream(keyFilename);
        KeyStore keystore = KeyStore.getInstance("JCEKS");
        keystore.load(is, password);

        Key key = keystore.getKey(alias, password);
        PrivateKey privateKey=(PrivateKey) key;

        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", "BC");
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(data);
        return ecdsaSign.sign();
    }

    public boolean verifySignature(byte[] data,byte[] signature,String crtAlias) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        FileInputStream is = new FileInputStream(keyFilename);
        KeyStore keystore = KeyStore.getInstance("JCEKS");
        keystore.load(is, password);

        java.security.cert.Certificate cert = keystore.getCertificate(crtAlias);
        PublicKey publicKey = cert.getPublicKey();

        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", "BC");
        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(data);
        return ecdsaVerify.verify(signature);
    }
}
