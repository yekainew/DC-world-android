package com.jkkg.hhtx.utils.aes;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    // 加密
    public static String Encrypt(String sSrc, String sKey){
        if (sKey == null) {
//            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
//            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = new byte[0];
        try {
            raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            //此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return Base64Encoder.encode(encrypted);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 解密
    public static String Decrypt(String sSrc, String sKey){
        try {
            // 判断Key是否正确
            if (sKey == null) {
//                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
//                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            //先用base64解密
            byte[] encrypted1 =Base64Decoder.decodeToBytes(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
//                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
//            System.out.println(ex.toString());
            return null;
        }
    }
    //    public static void main(String[] args) throws Exception {
//        /*
//         * 此处使用AES-128-ECB加密模式，key需要为16位。
//         */
////        String cKey = StringUtils.MD5("20190611");
//        String cKey ="be84647e81af5109";
//
//        // 需要加密的字串
//        String cSrc = "ABC";
//        System.out.println(cKey);
////        // 加密
////        String enString = AES.Encrypt(cSrc, cKey);
////        System.out.println("加密后的字串是：" + enString);
//        // 解密
//        String DeString = AES.Decrypt("UkTnlQiwDVNwxZEuhVsE1ax8ZxO6LjbpTWTyOdE7G8FNDCJxwMEh865i22Fw+pDJI9lOPmTmSRtQbvVyhx2uDz7RMqkp/2LonVeY1vW49Fxy5/tCzd3F/0GAH6FZH90VMinYZff+0d0bqWCGw0V8MTpTHNum1yvvFVi2aXNOwSMSiBLdwcnyL6Bd4TsPBsOG7B7/eqy05z6D9bRI95d5OTjjsn3nLS+H1VS/k5zpCsUQnricgYkeJnoNnAgoguEcnu8ICHgcOU+1p3+EMEwwqSphS3XylTRba7mOV8ifbH7zOnlbnim0/ofTaE5eU45jFPnRjZ1YcmX2SG2Cx6622WSELOwhYjOg3ARysqtihv3cHgJREhp0nQHjDDyihmS8t4tJZjXlsQXy7aU8sc/tzCBLCERHhWLAiKBOjHdsqFMOL7aIGs9Pr92Jvd8RirQQyA6Lg2oaKb/dgA+yn33bFUe7lLboHR0Q0s+jYNfQL54UojAaLeqdMLIBihoawC11vwePJh/3ZkpVFbZ47g5xHz7y20k10vKqmF6ZHfUhhuxTEF3JNHdgS2gprikRqksm9nN8FVIXLYdlVTQiicOkO8OLuvFjkU8TrGhBLTtbaONtGJNYHsU2ZLPXAiRE0l473I275yfrrw5/k6SLG0F8BsryBSUZs5u4G7FlaqKV1i+1yiHgvnLtizkhvdX13pkk7chq8CvHGs2jEYB/BdnZcC8cn1rip8RoTdQfHjUhVCkF1vbDLLwLyDYfzIuf4IYC", cKey);
//        System.out.println("解密后的字串是：" + DeString);
//    }
}