package lu.pata.fsync.api;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.spec.KeySpec;

public class CryptTool {
    private String secretKey;
    private String salt;

    public CryptTool(String secretKey, String salt) {
        this.secretKey = secretKey;
        this.salt = salt;
    }

    public byte[] encrypt(byte[] data) throws IOException {
        return process(data,Cipher.ENCRYPT_MODE);
    }

    public byte[] decrypt(byte[] data)    {
        return process(data,Cipher.DECRYPT_MODE);
    }

    private byte[] process(byte[] data, int mode){
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(mode, secretKey, ivspec);

            return cipher.doFinal(data);
        } catch (Exception ex){
            System.out.println(("Error while cryptographic processing: " + ex.toString()));
            return null;
        }
    }
}
