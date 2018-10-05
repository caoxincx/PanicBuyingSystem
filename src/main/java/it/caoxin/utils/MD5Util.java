package it.caoxin.utils;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    private static final String salt = "1a2b3c4d";
    /**
     * 将一个字符窜转化成MD5
     * @param src
     * @return
     */
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    /**
     * 将前端的传过来的MD5密码通过salt加密成from库使用的密码
     * @param passwordV
     * @param salt
     */
    public static String inputPassToFormPass(String passwordV, String salt) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + passwordV +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * 将from表单传过来的MD5密码加密成数据库密码
     * @param password
     * @param salt
     * @return
     */
    public static String fromPassToDBPass(String password, String salt) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + password +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPasswordToDBPass(String pass,String salt){
        String fromPass =inputPassToFormPass(pass,salt);
        String dbPass = fromPassToDBPass(fromPass, salt);
        return dbPass;
    }
}
