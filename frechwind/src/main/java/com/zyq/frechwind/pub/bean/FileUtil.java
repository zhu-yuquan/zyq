package com.zyq.frechwind.pub.bean;

import com.zyq.frechwind.base.ApplicationContext;
import com.zyq.frechwind.base.YmlConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 文件读取工具类
 */
public class FileUtil {
    private static Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 根据文件路径读取byte[] 数组
     */
    public static byte[] readFileByBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
            BufferedInputStream in = null;

            try {
                in = new BufferedInputStream(new FileInputStream(file));
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len1);
                }

                byte[] var7 = bos.toByteArray();
                return var7;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

                bos.close();
            }
        }
    }

    //将一个数据库里存储的路径转化成磁盘路径
    public static File toUploadFile(String uploadPath) {
        File uplaodRoot = new File(ApplicationContext.getInstance().getUploadRoot());
        return new File(uplaodRoot.getParent() + uploadPath);
    }

    public static String changeExtName(String absolutePath, String newExtName) {
        int pos = absolutePath.lastIndexOf(".");
        String newPath = absolutePath.substring(0, pos) + "." + newExtName;
        return newPath;
    }

    public static String getExtName(File file) {
        return getExtName(file.getAbsoluteFile());
    }

    public static String getExtName(String absolutePath) {
        int pos = absolutePath.lastIndexOf(".");
        String extName = absolutePath.substring(pos + 1);
        return extName;
    }

    public static String sizeExp(long fileSize) {
        if (fileSize < 1024) {
            return fileSize + "";
        }
        if (fileSize < 1024 * 1024) {
            return new BigDecimal(fileSize / 1024.00).setScale(2, BigDecimal.ROUND_HALF_UP) + " k";
        }
        if (fileSize >= (1024 * 1024)) {
            return new BigDecimal(fileSize / (1024.00 * 1024)).setScale(2, BigDecimal.ROUND_HALF_UP) + " M";
        }

        return "文件尺寸大小出现计算错误。";
    }

    //把file对象的物理磁盘路径，转换成相对路径，加上域名即可访问。
    public static String relativePath(File file) {
        String root = ApplicationContext.getInstance().getUploadRoot();
        root = root.substring(0, root.lastIndexOf("/upload"));

        String path = file.getAbsolutePath();
        path = path.replaceAll("\\\\", "/");
        return path.substring(root.length());
    }

    public static boolean isThumbnail(String path) {
        path = path.replaceAll("\\\\", "/");
        Pattern pattern = Pattern.compile("/(\\w+)_(\\d+)x(\\d+)\\.(\\w+)");
        Matcher matcher = pattern.matcher(path);
        boolean is = matcher.find();
        return is;
    }

    public static File newFile(String extName) {
        return newFile(extName, null);
    }

    public static File newFile(String extName, File backFile) {
        return newFile(null, extName, backFile);
    }

    public static File newFile(String appFolder, String extName, File backFile) {
        //生成新的文件名
        String folderName = DateFormatUtils.format(new Date(), "yyyyMMdd");//获取当前时间
        String random = RandomStringUtils.random(4, false, true);
        String newFileName = DateFormatUtils.format(new Date(), "HHmmss") + random;//生成文件名

        String uploadRoot = ApplicationContext.getInstance().getUploadRoot();
        if (YmlConfig.isDevEnv()){
            String[] str = uploadRoot.split(":");
            uploadRoot = str[1];
        }
        if (backFile != null) {
            uploadRoot = ApplicationContext.getInstance().getBackupRoot();
            String name = backFile.getName();
            newFileName = name.substring(0, name.lastIndexOf("."));
        }
        String filePath = uploadRoot + "/zyq/" + folderName;
        if(StringUtils.isNotBlank(appFolder)){
            filePath += "/" + appFolder;
        }
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String dirPath = dir.getAbsolutePath();
        if (YmlConfig.isDevEnv()){
            String[] str = dirPath.split(":");
            dirPath = str[1];
        }
        StringBuilder path = new StringBuilder(dirPath);

        path.append("/" + newFileName);

        path.append("." + extName);

        return new File(path.toString());
    }

    /**
     * 一个文件对应一个备份文件
     *
     * @param file
     * @return
     */
    public static File getBackupFile(File file) {
        String path = file.getAbsolutePath();
        String uploadRoot = ApplicationContext.getInstance().getUploadRoot();
        String backupRoot = ApplicationContext.getInstance().getBackupRoot();
        path = backupRoot + path.substring(uploadRoot.length());
        File backupFile = new File(path);
        backupFile.getParentFile().mkdirs();
        return backupFile;
    }

    public static HttpServletResponse download(File file, HttpServletResponse response) {
        try {
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }
}
