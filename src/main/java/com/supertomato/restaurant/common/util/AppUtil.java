package com.supertomato.restaurant.common.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * @author DiGiEx
 */
@Component
public class AppUtil {

    public static final Random RANDOM = new SecureRandom();
    public static final String UTF8_BOM = "\uFEFF";

    /**
    * Encrypt a String to Hash MD5
    * @param value
    * @return
    * @throws NoSuchAlgorithmException
    */
    public static String encryptMD5(String value) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(value.getBytes());
        byte byteData[] = md.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
    /**
    * Generate random password
    * @param len
    * @return
    */
    public static String generateRandomPassword(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghi"
                +"jklmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    /**
     * Generate Salt for the password
     *
     * @return
     */
    public static String generateSalt() {
        byte[] salt = new byte[Constant.SALT_LENGTH];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }


    public static String getFileExtension(MultipartFile file) {
        String extensionType = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extensionType != null) {
            extensionType = extensionType.toLowerCase();
        }
        return extensionType;
    }

    public static String replaceCharacter(String text, char replaceChar, int index) {
        // convert the given string to a character array
        char[] chars = text.toCharArray();

        // replace character at the specified position in a char array
        chars[index] = replaceChar;

        // convert the character array back into a string
        text = String.valueOf(chars);
        return String.valueOf(chars);
    }



}
