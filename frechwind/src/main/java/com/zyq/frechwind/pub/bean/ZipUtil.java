package com.zyq.frechwind.pub.bean;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    //压缩文件
    public void createZip(String zipFileName, File inputFile) throws Exception {
        System.out.println("压缩中...");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                zipFileName));
        BufferedOutputStream bo = new BufferedOutputStream(out);
        zip(out, inputFile, inputFile.getName(), bo);
        bo.flush();
        bo.close();
        out.close(); // 输出流关闭
        System.out.println("压缩完成");
    }

    /**
     * 对多个文件压缩
     */
    public void fileListZip(String root, String zipFileName, List<FilePro> filePath) throws Exception {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(zipFileName));
        ZipOutputStream zos = new ZipOutputStream(os);
        ZipEntry ze = null;
        BufferedInputStream bis = null;
        byte[] buf = new byte[8192];
        int len;
        root = root.replace("\\\\", "/");
        for (int i = 0; i < filePath.size(); i++) {
            String realPath = filePath.get(i).getFilePath();
            File file = new File(root + realPath.replaceAll("/upload", ""));
            if (!file.isFile()) continue;
            ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);
            bis = new BufferedInputStream(new FileInputStream(file));
            while ((len = bis.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }
        }
        zos.closeEntry();
        zos.close();
        bis.close();
        System.out.println(filePath.size());
    }

    private void zip(ZipOutputStream out, File f, String base,
                     BufferedOutputStream bo) throws Exception { // 方法重载
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            if (fl.length == 0) {
                out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base
                System.out.println(base + "/");
            }
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + "/" + fl[i].getName(), bo); // 递归遍历子文件夹
            }
//            System.out.println("第" + k + "次递归");
//            k++;
        } else {
            out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
            System.out.println(base);
            FileInputStream in = new FileInputStream(f);
            BufferedInputStream bi = new BufferedInputStream(in);
            int b;
            while ((b = bi.read()) != -1) {
                bo.write(b); // 将字节流写入当前zip目录
            }
            bo.flush();
            bi.close();
            in.close(); // 输入流关闭
        }
    }

    //查询，压缩文件的个数
    public int query(String catalog) {
        File[] list = new File(catalog).listFiles();
        int z = 0, zi = 0;
        long length = 0;
        for (File file : list) {
            if (file.isFile()) {
                z++;
                length += file.length();
            } else {
                zi++;
            }
        }
        return z;

    }

    //计算被压缩目录下 的文件总大小
    public double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“k”为单位
                double size = (double) file.length() / 1024;
                return size;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }
}
