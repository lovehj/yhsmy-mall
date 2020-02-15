package com.yhsmy.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * @auth 李正义
 * @date 2019/12/14 13:30
 **/
public class Base64Utils {

    public static final Logger logger = LoggerFactory.getLogger (Base64Utils.class);

    public static String io2Base64(InputStream inputStream) {
        String base64 = null;
        try{
            byte[] bytes = new byte[inputStream.available ()];
            inputStream.read (bytes);
            base64 = Base64.encodeBase64String (bytes);
            inputStream.close ();
        }catch (Exception e) {
            logger.error ("图片转64编码异常", e);
        }
        return base64;
    }

}
