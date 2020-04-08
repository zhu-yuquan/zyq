package com.zyq.frechwind.pub.service;

/**
 * Created by Administrator on 2017-12-07.
 */
public class ImageCalcWidthHeightRectangle {
    public int width;
    public int height;
    public long area;

    public ImageCalcWidthHeightRectangle(int width, int height) {
        this.width = width;
        this.height = height;
        area = width * height;
    }

    /**
     * 返回面积较大的那个
     *
     * @param r1
     * @param r2
     * @return
     */
    private static ImageCalcWidthHeightRectangle getBigger(ImageCalcWidthHeightRectangle r1, ImageCalcWidthHeightRectangle r2) {
        if (r1 == null) {
            return r2;
        }
        if (r2 == null) {
            return r1;
        }
        if (r1.area > r2.area) {
            return r1;
        } else {
            return r2;
        }
    }

    /**
     * 返回面积较大的那个
     *
     * @param r1
     * @param r2
     * @return
     */
    private static ImageCalcWidthHeightRectangle getSmaller(ImageCalcWidthHeightRectangle r1, ImageCalcWidthHeightRectangle r2) {
        if (r1 == null) {
            return r2;
        }
        if (r2 == null) {
            return r1;
        }
        if (r1.area > r2.area) {
            return r2;
        } else {
            return r1;
        }
    }

    public static ImageCalcWidthHeightRectangle getCutSize(int W, int H, int w, int h) {
        return scale(W, H, w, h, true);
    }

    public static ImageCalcWidthHeightRectangle getFillerSize(int W, int H, int w, int h) {
        ImageCalcWidthHeightRectangle r = scale(W, H, w, h, false);
        return r;
    }

    /**
     * 取得缩放之后的宽和高
     *
     * @param W
     * @param H
     * @param w
     * @param h
     * @param cutFlag 是否要进行裁切
     * @return
     */
    private static ImageCalcWidthHeightRectangle scale(int W, int H, int w, int h, boolean cutFlag) {
        //1. 按照宽度高度各缩放一次
        //组合一，根据宽度缩放：w-xh

        ImageCalcWidthHeightRectangle r1 = null;
        if (w < W || h == 0) {
            int newHeight = w * H / W;
            r1 = new ImageCalcWidthHeightRectangle(w, newHeight);
        }
        //组合二，根据高度缩放：h - xw

        ImageCalcWidthHeightRectangle r2 = null;
        if (h < H || w == 0) {
            int newWidth = h * W / H;
            r2 = new ImageCalcWidthHeightRectangle(newWidth, h);
        }
        if (cutFlag) {
            //如果裁切，那么取面积大的那个。
            return ImageCalcWidthHeightRectangle.getBigger(r1, r2);
        } else {
            //如果补白，那么取面积小的那个。
            return ImageCalcWidthHeightRectangle.getSmaller(r1, r2);
        }
    }
}
