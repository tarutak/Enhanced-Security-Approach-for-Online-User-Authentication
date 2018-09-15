/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

//import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.sun.xml.wss.impl.misc.Base64;
import constants.Constants;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class EncDecUtil implements Constants {

    public static String decryptAES(String enc) {
        String text = "";
        try {
            Cipher cipher = Cipher.getInstance(AES);
            SecretKeySpec spec = new SecretKeySpec(KEY.getBytes(UTF_8), AES);
            cipher.init(Cipher.DECRYPT_MODE, spec);
            byte[] raw = Base64.decode(enc);
            text = new String(cipher.doFinal(raw), UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return text;
    }

    public static String encryptAES(String text) {
        String enc = null;
        try {
            Cipher cipher = Cipher.getInstance(AES);
            SecretKeySpec spec = new SecretKeySpec(KEY.getBytes(UTF_8), AES);
            cipher.init(Cipher.ENCRYPT_MODE, spec);
            byte[] bytes = text.getBytes(UTF_8);
            byte[] raw = cipher.doFinal(bytes);
            enc = Base64.encode(raw);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return enc;
    }

    public static void main(String[] args) {
        String data = "nirr;1;909090909090909;90909";
        String enc = encryptAES(data);
        String dec = decryptAES("FvYi0x5Qgz0ORaTosghf7g==");
        System.out.println(data + " " + enc + " " + dec);
    }

}