package com.zyq.frechwind.base;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {
    /**
     * 按最大宽高来缩放图片(图片自适应最大宽高)
     * @param bitmap 原图
     * @param width   最大的宽
     * @param height  最大的高
     * @param type  图片格式
     * @param temp  输出的文件
     * @return  缩放完后图片的宽和高（int[0]为宽，int[1]为高）
     * @throws IOException
     */
    public static int[] zoom(BufferedImage bitmap, int width, int height, String type, File temp) throws IOException {
        if (bitmap == null) {
            return null;
        }
        if (width < 1 || height < 1) {
            return null;
        }
        Image itemp = null;
        float oldWidth = bitmap.getWidth();
        float oldHeight = bitmap.getHeight();
        double ratio = (height / oldHeight) > (width / oldWidth) ? (height / oldHeight) : (width / oldWidth);// 缩放比例
        itemp = bitmap.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);

        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
        itemp = op.filter(bitmap, null);
        ImageIO.write((BufferedImage) itemp, type, temp);
        int[] wh={itemp.getWidth(null),itemp.getHeight(null)};
        return wh;

    }

    /**
     * 绘制缩放图
     * @param img
     *            原图
     * @param width
     *            目标图宽
     * @param height
     *            目标图高
     * @return
     */

    private static BufferedImage makeThumbnail(Image img, int width, int height) {

        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(img.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        g.dispose();
        return tag;

    }



    /**
     * 裁剪图片
     * @param image
     *            原图
     * @param subImageBounds
     *            裁剪矩形框
     * @param subImageFile
     *            保存路径
     * @throws IOException
     */
    public static void saveSubImage(BufferedImage image, Rectangle subImageBounds, File subImageFile) throws IOException {

        String fileName = subImageFile.getName();
        String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);
        BufferedImage subImage = new BufferedImage(subImageBounds.width, subImageBounds.height, BufferedImage.TYPE_INT_RGB);

        Graphics g = subImage.getGraphics();

        if (subImageBounds.width > image.getWidth() || subImageBounds.height > image.getHeight()) {

            int left = subImageBounds.x;
            int top = subImageBounds.y;

            if (image.getWidth() < subImageBounds.width)
                left = (int) ((subImageBounds.width - image.getWidth()) / 2);

            if (image.getHeight() < subImageBounds.height)
                top = (int) ((subImageBounds.height - image.getHeight()) / 2);

            g.setColor(Color.white);
            g.fillRect(0, 0, subImageBounds.width, subImageBounds.height);
            g.drawImage(image, left, top, null);
            ImageIO.write(image, formatName, subImageFile);

        } else {
            // BufferedImage subImage =
            // image.getSubimage(subImageBounds.x,subImageBounds.y,
            // subImageBounds.width, subImageBounds.height);
            g.drawImage(image.getSubimage(subImageBounds.x, subImageBounds.y, subImageBounds.width, subImageBounds.height), 0, 0, null);
        }
        g.dispose();
        ImageIO.write(subImage, formatName, subImageFile);
    }



    /**
     * 图片缩放裁剪并保存到指定文件
     * @param srcImageFile
     *            原图保存路径
     * @param descDir
     *            目标图保存路径
     * @param width
     *            缩放后图片宽度
     * @param height
     *          缩放后图片高度
     * @param rect
     *            裁剪矩形框
     * @throws IOException
     */
    public static void cut(String srcImageFile, String descDir, int width, int height, Rectangle rect) throws IOException {
        Image image = javax.imageio.ImageIO.read(new File(srcImageFile));
        BufferedImage bImage = makeThumbnail(image, width, height);
        saveSubImage(bImage, rect, new File(descDir));

    }

    /**
     *  图片缩放裁剪并保存到指定文件
     * @param srcImageFile 原图保存路径
     * @param descDir 目标图保存路径
     * @param width  缩放后图片宽度
     * @param height  缩放后图片高度
     * @param rect   裁剪矩形框
     * @throws IOException
     */
    public static void cut(File srcImageFile, File descDir, int width, int height, Rectangle rect) throws IOException {
        Image image = javax.imageio.ImageIO.read(srcImageFile);
        BufferedImage bImage = makeThumbnail(image, width, height);
        saveSubImage(bImage, rect, descDir);
    }

    /**
     * 裁切文件的指定部分并保存到指定文件
     * @param tFile  要裁切的文件
     * @param sFile  裁切后的文件
     * @param x     裁切的起始X坐标
     * @param y   裁切的起始Y坐标
     * @param w    裁切的宽
     * @param h  裁切的高
     * @return 是否成功
     * @throws IOException
     */
    public static boolean cutToFile(File tFile, File sFile, int x, int y, int w, int h) throws IOException {

        BufferedImage tBi = ImageIO.read(tFile);
        Rectangle rec = new Rectangle(x, y, w, h);
        saveSubImage(tBi, rec, sFile);
        return true;

    }

}
