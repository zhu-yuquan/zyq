package com.zyq.frechwind.base;

import com.alibaba.simpleimage.ImageWrapper;
import com.alibaba.simpleimage.SimpleImageException;
import com.alibaba.simpleimage.util.ImageReadHelper;
import com.zyq.frechwind.pub.bean.FileUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(filterName = "uploadImgFilter", urlPatterns = "/upload/*")
public class UploadImgFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(UploadImgFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        uri = uri.substring(7);

        Pattern pattern = Pattern.compile("/(\\w+)_(\\d+)x(\\d+)\\.(\\w+)");
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            String imgName = matcher.group(1);
            int width = Integer.parseInt(matcher.group(2));
            int height = Integer.parseInt(matcher.group(3));
            String extName = matcher.group(4);

            int pos = uri.lastIndexOf("/");
            String path = uri.substring(0, pos);

            String backUploadRoot = ApplicationContext.getInstance().getBackupRoot();
            String uploadRoot = ApplicationContext.getInstance().getUploadRoot();

            File smallImgFile = new File(uploadRoot + uri);
            //log.info("smallImgFile smallImgFile.exists():" + smallImgFile.exists());
            if (!smallImgFile.exists()) {
                //如果小图不存在
                String bigImgFile = backUploadRoot + path + "/" + imgName + "." + extName;
                File bigFile = new File(bigImgFile);

                //视频截图的时候，由于异步的原因，无法马上读取到大图，因此需要特殊处理一下。
                if (!bigFile.exists()) {
                    bigImgFile = uploadRoot + path + "/" + imgName + "." + extName;
                    bigFile = new File(bigImgFile);
                }
                //log.info("bigFile:" + bigFile);
                if (bigFile.exists()) {
                    //如果大图存在
                    ImageImageTwo.compress(new FileInputStream(bigFile),smallImgFile,width,height);
                } else {
                    logger.info("can't find bigFile:" + bigImgFile);
                }
            }
        }

        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public void destroy() {

    }


    /**
     * 按照宽度或高度中较小的一边进行缩放，然后裁切
     *
     * @param in
     * @param out
     * @param w
     * @param h
     */
    public final static void scaleAndCut(File in, File out, int w, int h) throws IOException {
        Thumbnails.of(in).size(w, h).toFile(out);
    }
}