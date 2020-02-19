package com.yhsmy.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.*;

/**
 * @auth 李正义
 * @date 2020/2/12 22:29
 **/
@Slf4j
public class WechatUtil {

    public static boolean checkSignature (String token, String signature, String timestamp, String nonce) {
        // 将token、timestamp、nonce三个参数进行字典序排序
        String[] arr = new String[]{token, timestamp, nonce};
        Arrays.sort (arr);
        StringBuilder content = new StringBuilder ();
        int arrLen = arr.length;
        for (int i = 0; i < arrLen; i++) {
            content.append (arr[i]);
        }
        MessageDigest messageDigest = null;
        String temp = null;
        try {
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            messageDigest = MessageDigest.getInstance ("SHA-1");
            temp = byteToStr (messageDigest.digest (content.toString ().getBytes ()));
        } catch (Exception e) {
            log.error ("微信认证加密失败！", e);
        }
        content = null;
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return StringUtils.isBlank (temp) ? false : temp.equals (signature.toUpperCase ());
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param digests
     * @return
     */
    private static String byteToStr (byte[] digests) {
        String digest = "";
        int len = digests.length;
        for (int i = 0; i < len; i++) {
            digest += byteToHexStr (digests[i]);
        }
        return digest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param digest
     * @return
     */
    private static String byteToHexStr (byte digest) {
        char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = hex[(digest >>> 4) & 0X0F];
        tempArr[1] = hex[digest & 0X0F];
        return new String (tempArr);
    }

    public static Map<String, String> parseXml (InputStream in) throws DocumentException, IOException {
        Map<String, String> result = new HashMap<> (1);
        // 读取输入流
        SAXReader reader = new SAXReader ();
        Document doc = reader.read (in);
        // 得到xml根元素
        Element root = doc.getRootElement ();
        // 得到根元素的所有子节点
        List<Element> childEle = root.elements ();
        for (Element ele : childEle) {
            result.put (ele.getName (), ele.getText ());
        }
        in.close ();
        in = null;
        return result;
    }

    public static String beanToMap(Object data) {
        try{
           Map<String, String> map =  BeanUtils.describe (data);
           return mapToXml (map);
        }catch (Exception e){
            log.error ("javaBean转Map出错！", e);
        }
        return "";
    }

    /**
     * 转换Map为XML
     *
     * @param map
     * @return
     */
    public static String mapToXml (Map map) {
        StringBuilder builder = new StringBuilder ("<xml>");
        mapToXml (map, builder);
        builder.append ("</xml>");
        return builder.toString ();
    }

    @SuppressWarnings({"unchecked"})
    public static void mapToXml (Map map, StringBuilder builder) {
        Set set = map.keySet ();
        for (Iterator it = set.iterator (); it.hasNext (); ) {
            String key = (String) it.next (), _key = key;
            char pirfix = key.charAt (0);
            if (Character.isLowerCase (pirfix)) { // 首字母小写的要转大写
                _key = _key.substring (1, _key.length ());
                _key = String.valueOf (pirfix).toUpperCase () + _key;
            }
            Object value = map.get (key);
            if (value == null) {
                value = "";
            }

            if (value.getClass ().getName ().equals ("java.util.ArrayList")) {
                ArrayList arrayList = (ArrayList) map.get (key);
                builder.append ("<" + _key + ">");
                int size = arrayList.size ();
                for (int i = 0; i < size; i++) {
                    HashMap hashMap = (HashMap) arrayList.get (i);
                    mapToXml (hashMap, builder);
                }
                builder.append ("</" + _key + ">");
            } else {
                if (value instanceof HashMap) {
                    builder.append ("<" + _key + ">");
                    mapToXml ((HashMap) value, builder);
                } else {
                    builder.append ("<" + _key + "><![CDATA[" + value + "]]></" + _key + ">");
                }
            }
        }
    }


}
