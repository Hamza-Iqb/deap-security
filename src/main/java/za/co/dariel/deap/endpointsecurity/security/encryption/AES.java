package za.co.dariel.deap.endpointsecurity.security.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class AES {
    private static final String SECRET_KEY = "my_super_secret_key_ho_ho_ho";
    private static final String SALT = "ssshhhhhhhhhhh!!!!";
    private static final Logger logger = LoggerFactory.getLogger(AES.class);

    public static String encrypt(String strToEncrypt) {
        try {
        	var random = new SecureRandom();
        	
        	var iv = new byte[16];
            random.nextBytes(iv);
            var ivspec = new IvParameterSpec(iv);

            var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            var secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            logger.error("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt) {
        try {
        	var random = new SecureRandom();
        	
        	var iv = new byte[16];
            random.nextBytes(iv);
            var ivspec = new IvParameterSpec(iv);

            var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            var secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            var cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            logger.error("Error while decrypting: " + e.toString());
        }
        return null;
    }

}