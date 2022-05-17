package it.unisa.passchain.utils;

public class Crypto {

    /**
     * Codifica una stringa usando come chiave "salt".
     */
    public static String encode(String plaintext, String salt){
        StringBuilder secure_string = new StringBuilder();
        int count = 0;
        for (int i = 0; i < plaintext.length(); i++) {
            char character = plaintext.charAt(i);
            int num = (int) character - (int) salt.charAt(count);

            if (num < 0)
                num -= 255;
            secure_string.append(num);

            if (count < salt.length() - 1)
                count += 1;
            else count = 0;
        }
        return secure_string.toString();
    }

    /**
     * Decodifica una stringa usando come chiave "salt".
     */
    public static String decode(String encoded_text, String salt) {
        StringBuilder plaintext = new StringBuilder();
        int count = 0;
        for (int i = 0; i < encoded_text.length(); i++) {
            char character = encoded_text.charAt(i);
            int temp_string = (int) character - (int) salt.charAt(count);

            if (temp_string < 0)
                temp_string += 255;
            plaintext.append(temp_string);

            if (count < salt.length() - 1)
                count += 1;
            else count = 0;
        }
        return plaintext.toString();
    }
}