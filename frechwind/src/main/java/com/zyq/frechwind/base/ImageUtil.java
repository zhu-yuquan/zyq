package com.zyq.frechwind.base;

import com.alibaba.simpleimage.ImageFormat;
import com.alibaba.simpleimage.ImageWrapper;
import com.alibaba.simpleimage.SimpleImageException;
import com.alibaba.simpleimage.render.CropParameter;
import com.alibaba.simpleimage.render.ScaleParameter;
import com.alibaba.simpleimage.render.ScaleParameter.Algorithm;
import com.alibaba.simpleimage.render.WatermarkParameter;
import com.alibaba.simpleimage.render.WriteParameter;
import com.alibaba.simpleimage.util.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.zyq.frechwind.pub.bean.FileUtil;
import com.zyq.frechwind.pub.service.ImageCalcWidthHeightRectangle;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtil {

    public static void main(String[] args) throws Exception {
        rotate(new File("c:/hh.jpg"), 90, new File("c:/hh90.jpg"));
    }

    protected static ImageFormat outputFormat = ImageFormat.JPEG;

    /**
     * @param pInput
     * @param pImgeFlag
     * @return
     * @throws Exception
     */
    public static boolean isPicture(String pInput, String pImgeFlag) throws Exception {
        if (StringUtils.isBlank(pInput)) {
            return false;
        }
        String tmpName = pInput.substring(pInput.lastIndexOf(".") + 1, pInput.length());
        String imgeArray[][] = {{"bmp", "0"}, {"dib", "1"}, {"gif", "2"}, {"jfif", "3"}, {"jpe", "4"}, {"jpeg", "5"}, {"jpg", "6"}, {"png", "7"}, {"tif", "8"},
                {"tiff", "9"}, {"ico", "10"}};
        for (int i = 0; i < imgeArray.length; i++) {
            if (!StringUtils.isBlank(pImgeFlag) && imgeArray[i][0].equals(tmpName.toLowerCase()) && imgeArray[i][1].equals(pImgeFlag)) {
                return true;
            }
            if (StringUtils.isBlank(pImgeFlag) && imgeArray[i][0].equals(tmpName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isImgName(String absolutPath) {
        String extName = "";
        if (absolutPath.indexOf(".") == -1) {
            extName = absolutPath;
        } else {
            extName = FileUtil.getExtName(absolutPath);
        }
        return (extName.equalsIgnoreCase("jpeg") || extName.equalsIgnoreCase("jpg") || extName.equalsIgnoreCase("png"));
    }



    private static BufferedImage fillerWhite(BufferedImage src, int w, int h) {
        BufferedImage b = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = b.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, b.getWidth(), b.getHeight());
        g2d.drawImage(src, (w - src.getWidth()) / 2, (h - src.getHeight()) / 2, src.getWidth(), src.getHeight(), null);
        g2d.dispose();
        return b;
    }



    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum WatermarkPosition implements PojoEnum {
        RightTop("右上"), LeftBottom("左下"), RightBottom("右下"), Center("居中");

        private String title;

        private WatermarkPosition(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


        public String getName() {
            return name();
        }

    }


    /**
     * @param sourchFile       ：源图片路径
     * @param targetFile       ：生成后的目标图片路径
     * @param markContent      :要加的文字
     * @param markContentColor :文字颜色
     * @param qualNum          :质量数字
     * @param fontType         :字体类型
     * @param fontSize         :字体大小
     * @return
     */
    public static void createMark(File sourchFile, File targetFile,
                                  String markContent, Color markContentColor, float qualNum,
                                  String fontType, int fontSize) throws IOException {
        int w = 0, h = 0;
        /* 构建要处理的源图片 */
        ImageIcon imageIcon = new ImageIcon(sourchFile.getAbsolutePath());
        /* 获取要处理的图片 */
        Image image = imageIcon.getImage();
        // Image可以获得图片的属性信息
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        // 为画出与源图片的相同大小的图片（可以自己定义）
        BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 构建2D画笔
        Graphics2D g = bImage.createGraphics();
        /* 设置2D画笔的画出的文字颜色 */
        g.setColor(markContentColor);
        /* 设置2D画笔的画出的文字背景色 */
        g.setBackground(Color.white);
        /* 画出图片 */
        g.drawImage(image, 0, 0, null);
        /* --------对要显示的文字进行处理-------------- */
        AttributedString ats = new AttributedString(markContent);
        Font font = new Font(fontType, Font.BOLD, fontSize);
        g.setFont(font);
       /* 消除java.awt.Font字体的锯齿 */
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        /* 消除java.awt.Font字体的锯齿 */
//        更多实例请访问 http://www.walkerjava.com/forum.php?mod=viewthread&tid=135
        // font = g.getFont().deriveFont(30.0f);
        ats.addAttribute(TextAttribute.FONT, font, 0, markContent.length());
        AttributedCharacterIterator iter = ats.getIterator();
        /* 添加水印的文字和设置水印文字出现的内容 ----位置 */
        g.drawString(iter, width - w, height - h);
        /* --------对要显示的文字进行处理-------------- www.it165.net */
        g.dispose();// 画笔结束
        FileOutputStream out = null;
        try {
            // 输出 文件 到指定的路径
            out = new FileOutputStream(targetFile);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bImage);
            param.setQuality(qualNum, true);
            encoder.encode(bImage, param);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 缩放到指定宽度 高度自适应
     *
     * @param in
     * @param out
     * @param width
     */
    @SuppressWarnings("static-access")
    public final static void scaleWithWidth(File in, File out, Integer width) throws SimpleImageException, FileNotFoundException {
//        FileOutputStream outStream = null;
//        FileInputStream inStream = null;
//        try {
//            inStream = new FileInputStream(in);
//            ImageWrapper imageWrapper = ImageReadHelper.read(inStream);
//
//            int w = imageWrapper.getWidth();
//            int h = imageWrapper.getHeight();
//            // 1.缩放
//            ScaleParameter scaleParam = new ScaleParameter(w, h, Algorithm.LANCZOS); // 缩放参数
//            if (w < width) {// 如果图片宽和高都小于目标图片则不做缩放处理
//                scaleParam = new ScaleParameter(w, h, Algorithm.LANCZOS);
//            } else {
//                int newHeight = h * width / w;
//                scaleParam = new ScaleParameter(width, newHeight + 1, Algorithm.LANCZOS);
//            }
//            PlanarImage planrImage = ImageScaleHelper.scale(imageWrapper.getAsPlanarImage(), scaleParam);
//            imageWrapper = new ImageWrapper(planrImage);
//            // 4.输出
//            outStream = new FileOutputStream(out);
//            String prefix = out.getName().substring(out.getName().lastIndexOf(".") + 1);
//            ImageWriteHelper.write(imageWrapper, outStream, outputFormat.getImageFormat(prefix), new WriteParameter());
//        } finally {
//            IOUtils.closeQuietly(inStream); // 图片文件输入输出流必须记得关闭
//            IOUtils.closeQuietly(outStream);
//        }
    }

    //获取当前年月日
    public static String getNowTime() {
        //得到long类型当前时间
        long l = System.currentTimeMillis();
        //new日期对象
        Date date = new Date(l);
        //转换提日期输出格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String str = dateFormat.format(date);
        return str;
    }

    public static void rotate(File file, int degree) throws IOException {
        BufferedImage bi = ImageIO.read(file); // 读取该图片
        bi = rotate(bi, degree);
        ImageIO.write(bi, FileUtil.getExtName(file.getAbsolutePath()), file); // 保存图片
    }

    public static void rotate(File file, int degree, File outFile) throws IOException {
        BufferedImage bi = ImageIO.read(file); // 读取该图片
        bi = rotate(bi, degree);
        ImageIO.write(bi, FileUtil.getExtName(file.getAbsolutePath()), outFile); // 保存图片
    }

    /**
     * 旋转
     *
     * @param degree 旋转角度
     * @throws Exception
     */
    public static BufferedImage rotate(BufferedImage bi, int degree) {
        int swidth = 0; // 旋转后的宽度
        int sheight = 0; // 旋转后的高度
        int x; // 原点横坐标
        int y; // 原点纵坐标

        // 处理角度--确定旋转弧度
        degree = degree % 360;
        if (degree < 0)
            degree = 360 + degree;// 将角度转换到0-360度之间
        double theta = Math.toRadians(degree);// 将角度转为弧度

        // 确定旋转后的宽和高
        if (degree == 180 || degree == 0 || degree == 360) {
            swidth = bi.getWidth();
            sheight = bi.getHeight();
        } else if (degree == 90 || degree == 270) {
            sheight = bi.getWidth();
            swidth = bi.getHeight();
        } else {
            swidth = (int) (Math.sqrt(bi.getWidth() * bi.getWidth()
                    + bi.getHeight() * bi.getHeight()));
            sheight = (int) (Math.sqrt(bi.getWidth() * bi.getWidth()
                    + bi.getHeight() * bi.getHeight()));
        }

        x = (swidth / 2) - (bi.getWidth() / 2);// 确定原点坐标
        y = (sheight / 2) - (bi.getHeight() / 2);

        BufferedImage spinImage = new BufferedImage(swidth, sheight,
                bi.getType());
        // 设置图片背景颜色
        Graphics2D gs = (Graphics2D) spinImage.getGraphics();
        gs.setColor(Color.white);
        gs.fillRect(0, 0, swidth, sheight);// 以给定颜色绘制旋转后图片的背景

        AffineTransform at = new AffineTransform();
        at.rotate(theta, swidth / 2, sheight / 2);// 旋转图象
        at.translate(x, y);
        AffineTransformOp op = new AffineTransformOp(at,
                AffineTransformOp.TYPE_BICUBIC);
        spinImage = op.filter(bi, spinImage);
        return spinImage;
    }
}