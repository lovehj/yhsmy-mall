package com.yhsmy.utils;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

/**
 * 验证码
 *
 * @auth 李正义
 * @date 2019/11/6 22:43
 **/
public class VerifyCodeUtil {

    /**
     * 这里去掉了1,0,i,o这几个容易混淆的字符
     */
    public static final String VERIFY_CODE = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static Random random = new Random ();

    /**
     * 生成默认的验证码
     *
     * @return
     */
    public static String generateVerifyCode () {
        return generateVerifyCode (4, VERIFY_CODE);
    }

    /**
     * 指定验证码长度及验证码源
     *
     * @param size
     * @param verifyCode
     * @return
     */
    public static String generateVerifyCode (int size, String verifyCode) {
        if (size <= 0) {
            size = 4;
        }
        if (StringUtils.isBlank (verifyCode) || verifyCode.length () < 4) {
            verifyCode = VERIFY_CODE;
        }

        int codeLen = verifyCode.length ();
        Random rand = new Random (System.currentTimeMillis ());
        StringBuilder builder = new StringBuilder (size);
        for (int i = 0; i < size; i++) {
            builder.append (verifyCode.charAt (rand.nextInt (codeLen - 1)));
        }
        return builder.toString ();
    }

    /**
     * 输出随机验证码图片流,并返回验证码值
     *
     * @param w
     * @param h
     * @param outputFile
     * @return
     * @throws Exception
     */
    public static String outputVerifyImage (int w, int h, File outputFile) throws IOException {
        String verifyCode = generateVerifyCode ();
        outputImage (w, h, outputFile, verifyCode);
        return verifyCode;
    }

    public static void outputImage (int w, int h, File outFile, String verifyCode) throws IOException {
        if (outFile == null) {
            return;
        }
        File dir = outFile.getParentFile ();
        if (!dir.exists ()) {
            dir.mkdir ();
        }
        try {
            outFile.createNewFile ();
            FileOutputStream fos = new FileOutputStream (outFile);
            outputImage (w, h, fos, verifyCode);
            fos.close ();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 输出指定验证码图片流
     *
     * @param w
     * @param h
     * @param fos
     * @param verifyCode
     * @throws IOException
     */
    public static void outputImage (int w, int h, OutputStream fos, String verifyCode) throws IOException {
        int verifySize = verifyCode.length ();
        BufferedImage image = new BufferedImage (w, h, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random ();
        Graphics2D graphics2D = image.createGraphics ();
        graphics2D.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] colors = new Color[5];
        Color[] colorSpaces = new Color[]{Color.WHITE, Color.CYAN,
                Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
                Color.PINK, Color.YELLOW};
        float[] floats = new float[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorSpaces[rand.nextInt (colorSpaces.length)];
            floats[i] = rand.nextFloat ();
        }
        Arrays.sort (floats);

        graphics2D.setColor (Color.GRAY);// 设置边框色
        graphics2D.fillRect (0, 0, w, h);

        Color color = getRandColor (200, 250);
        graphics2D.setColor (color); // 设置背景色
        graphics2D.fillRect (0, 2, w, h - 4);

        Random r = new Random ();
        graphics2D.setColor (getRandColor (160, 200)); //设置线条的颜色

        for (int i = 0; i < 20; i++) {
            int x = r.nextInt (w - 1);
            int y = r.nextInt (h - 1);
            int xl = r.nextInt (6) + 1;
            int yl = r.nextInt (12) + 1;
            graphics2D.drawLine (x, y, x + xl + 40, y + yl + 20);
        }

        //添加噪点
        float yawpRate = 0.05f; // 噪声率
        int area = (int) (yawpRate * w * h);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt (w);
            int y = random.nextInt (h);
            int rgb = getRandomIntColor ();
            image.setRGB (x, y, rgb);
        }

        shear (graphics2D, w, h, color);// 使图片扭曲

        graphics2D.setColor (getRandColor (100, 160));
        int fontSize = h - 4;
        Font font = new Font ("Algerian", Font.ITALIC, fontSize);
        graphics2D.setFont (font);
        char[] chars = verifyCode.toCharArray ();
        for (int i = 0; i < verifySize; i++) {
            AffineTransform affine = new AffineTransform ();
            affine.setToRotation (Math.PI / 4 * rand.nextDouble () * (rand.nextBoolean () ? 1 : -1), (w / verifySize) * i + fontSize / 2, h / 2);
            graphics2D.setTransform (affine);
            graphics2D.drawChars (chars, i, 1, ((w - 10) / verifySize) * i + 5, h / 2 + fontSize / 2 - 10);
        }

        graphics2D.dispose ();
        ImageIO.write (image, "jpg", fos);
    }

    private static void shear (Graphics2D graphics2D, int w, int h, Color color) {
        shearX (graphics2D, w, h, color);
        shearY (graphics2D, w, h, color);
    }

    private static int getRandomIntColor () {
        int[] rgb = getRandomRgb ();
        int color = 0;
        for (int c : rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    private static int[] getRandomRgb () {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextInt (255);
        }
        return rgb;
    }

    private static Color getRandColor (int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt (bc - fc);
        int g = fc + random.nextInt (bc - fc);
        int b = fc + random.nextInt (bc - fc);
        return new Color (r, g, b);
    }

    private static void shearX (Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt (2);
        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt (2);
        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin ((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea (0, i, w1, 1, (int) d, 0);
            if (borderGap) {
                g.setColor (color);
                g.drawLine ((int) d, i, 0, i);
                g.drawLine ((int) d + w1, i, w1, i);
            }
        }
    }

    private static void shearY (Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt (40) + 10; // 50;
        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin ((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea (i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor (color);
                g.drawLine (i, (int) d, i, 0);
                g.drawLine (i, (int) d + h1, i, h1);
            }
        }
    }
}
