package com.zyq.frechwind.pub.service;

import com.alibaba.simpleimage.ImageWrapper;
import com.alibaba.simpleimage.SimpleImageException;
import com.alibaba.simpleimage.util.ImageReadHelper;
import com.zyq.frechwind.base.ApplicationContext;
import com.zyq.frechwind.base.ImageUtil;
import com.zyq.frechwind.pub.bean.FileUtil;
import com.zyq.frechwind.pub.bean.Upload;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SiteImgUtil {
    @Autowired
    private UploadService uploadService;

    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    /**
     * 在显示图片的时候，自动给图片文件名后面加上尺寸，并且自动生成一个小图
     * 比如：原来文件名为xxxx.jpg，调用之后，文件名变为：xxxxx_80x60.jpg
     * 该方法与一个过滤器配合使用UploadImgFilter，这里加了尺寸，那边会判断文件是否存在，如果不存在，会自动生成该尺寸。
     *
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static String addImgSizeOnShow(String path, int width, int height) {
        if (StringUtils.isEmpty(path)) {
            return "";
        }

        String size = "_" + width + "x" + height;
        if (path.indexOf(size) == -1) {
            if(path.indexOf(".") != -1) {
                String extName = FileUtil.getExtName(path);
                path = path.substring(0, path.lastIndexOf("."));
                path = path + size + "." + extName;
            }
        }

        return path;
    }

    public static String fitScreenOnShow(String html) {
        return fitScreenOnShow(html, 700);
    }

    /**
     * 在内容中有一些图片宽度过大，会导致手机端，横向出现滚动条，因此会把尺寸大于700像素的图片，自动改为width:100%，以避免横线滚动
     *
     * @param html
     * @return
     */
    public static String fitScreenOnShow(String html, int maxWidth) {
        if (StringUtils.isBlank(html)) {
            return "";
        }
        Pattern pattern = Pattern.compile("(<img[^>]+>)");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String img = matcher.group(1);

            Pattern p = Pattern.compile("width=\"(\\d+)\"");
            Matcher m = p.matcher(img);
            if (m.find()) {
                String width = m.group(1);
                Integer w = NumberUtils.createInteger(width);
                if (w < maxWidth) {
                    continue;
                }
            }

            String newImg = img.replaceAll("height=\"\\d+\"", "");
            newImg = newImg.replaceAll("width=\"\\d+\"", "width=\"100%\"");
            html = html.replace(img, newImg);
        }
        return html;
    }

    public static String zoomInImgOnSaveOrShow(String html) throws SimpleImageException {
        try {
            return zoomInImgOnSaveOrShow(html, 900);
        } catch (IOException e) {
            e.printStackTrace();
            return html;
        }
    }

    /**
     * 内容中有些图片过大，因此需要自动过滤，将图片缩小，避免图片太大，影响传输
     *
     * @param html
     * @return
     */
    public static String zoomInImgOnSaveOrShow(String html, int maxWidth) throws IOException, SimpleImageException {
        if (StringUtils.isBlank(html)) {
            return html;
        }
        Pattern pattern = Pattern.compile("(<img[^>]+>)");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String img = matcher.group(1);

            Pattern p = Pattern.compile("src=\"([^\"]+)\"");
            Matcher m = p.matcher(img);
            if (m.find()) {
                String src = m.group(1);
                if(src.toLowerCase().startsWith("http://") || src.toLowerCase().startsWith("https://")){
                    continue;
                }

                String root = ApplicationContext.getInstance().getUploadRoot();
                src = src.replace("/upload", "");
                String path = root + src;
                File file = new File(root + src);
                FileInputStream inStream = new FileInputStream(file);
                ImageWrapper imageWrapper = ImageReadHelper.read(inStream);
                if (imageWrapper.getWidth() > maxWidth) {
                    //这里处理图片，把图片读出来，缩小。
                    try {
                        ImageUtil.scaleWithWidth(new File(path), new File(path), maxWidth);
                    } catch (SimpleImageException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        html = fitScreenOnShow(html, maxWidth);
        return html;
    }

    private String getAttrValue(String html, String attrName) {
        return null;
    }

    /**
     * 将html中的文本内容过滤出来。
     *
     * @param content
     * @return
     */
    public static String text(String content) {
        if (StringUtils.isNotBlank(content)) {
            Document doc = Jsoup.parse(content);
            String text = doc.text();
//            doc.getElementsByAttributeStarting("");
            return text.trim();
        }
        return "";
    }

    /**
     * 图片上传的时候，自动居中，并且自动加一个样式在图片上面。
     *
     * @param content
     * @return
     */
    public static String karonImg(String content) {
        Document doc = Jsoup.parse(content);
        Elements karonImgDivs = doc.getElementsByClass("karonImg");

        if (karonImgDivs.size() == 0) {
            Elements imgs = doc.getElementsByTag("img");
            for (int i = 0; i < imgs.size(); i++) {
//                doc.createElement("div");
                //imgs.get(i).replaceWith();
                Element e = imgs.get(i);
                imgs.get(i).parent().append("<div style='width: 100%;background-color: #8e8e8e;padding: 30px;text-align: center'>" + e + "</div>");
                imgs.get(i).remove();
            }
        }

        return doc.html();
    }

    /**
     * 内容中的图片取出来，存到upload中
     *
     * @param html
     * @return
     */
    public void createImgByHtml(String html,String ownerType,String ownerId,String bizType){
        if (StringUtils.isBlank(html)) {
            return;
        }
        Pattern pattern = Pattern.compile("(<img[^>]+>)");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String img = matcher.group(1);

            Pattern p = Pattern.compile("src=\"([^\"]+)\"");
            Matcher m = p.matcher(img);
            if (m.find()) {
                String src = m.group(1);
                if(src.toLowerCase().startsWith("http://") || src.toLowerCase().startsWith("https://")){
                    continue;
                }

                String root = ApplicationContext.getInstance().getUploadRoot();
                src = src.replace("/upload", "");
                String path = root + src;
                File file = new File(path);
                String uploadRoot = ApplicationContext.getInstance().getUploadRoot();
                String absolutePath = file.getAbsolutePath().substring(uploadRoot.length() - "/upload".length());
                absolutePath = absolutePath.replaceAll("\\\\", "/");
                List<Upload> list = uploadService.list(ownerType, ownerId, bizType);
                if(list.size()==0){
                    this.uploadService.createUpload(file,file.getName(),ownerType,ownerId,bizType,0);
                }

            }
        }
    }
}