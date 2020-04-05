package com.zyq.frechwind.pub.service;

import com.zyq.frechwind.base.AppException;
import com.zyq.frechwind.base.ApplicationContext;
import com.zyq.frechwind.base.Finder;
import com.zyq.frechwind.base.Operator;
import com.zyq.frechwind.pub.bean.FileUtil;
import com.zyq.frechwind.pub.bean.Upload;
import com.zyq.frechwind.pub.bean.ZipUtil;
import com.zyq.frechwind.pub.dao.UploadDao;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UploadService {

    @Autowired
    private UploadDao uploadDao;

    public Upload upload(MultipartFile file,
                         String originalFilename,
                         String ownerType,
                         String ownerId,
                         String bizType,
                         Integer uploadMax,
                         Integer seq) throws IOException {
        //判断最大数量。如果超出，那么把前面的删除掉。
        if (uploadMax != null) {
            if (uploadMax > 0) {
                List<Upload> list = list(ownerType, ownerId, bizType);
                for (int i = uploadMax - 1; i < list.size(); i++) {
                    Upload upload = list.get(i);
                    this.delete(upload.getUploadId());
                }
            }
        }

        //newFileName = newFileName + extName;

        //处理文件数据
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;

        String extName = FileUtil.getExtName(originalFilename);
        File newFile = FileUtil.newFile(extName);
        try {
            byte[] buffer = file.getBytes();
            fos = new FileOutputStream(newFile);
            bos = new BufferedOutputStream(fos);
            bos.write(buffer);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //备份一份原图
        File backFile = FileUtil.getBackupFile(newFile);
        FileUtils.copyFile(newFile, backFile);

        Upload upload = createUpload(newFile, originalFilename, ownerType, ownerId, bizType, seq);

        return upload;
    }

    public Upload updateUpload(MultipartFile file,
                               String originalFilename,
                               String ownerType,
                               String ownerId,
                               String bizType,
                               String uploadId,
                               Integer uploadMax) throws IOException {
        //判断最大数量。如果超出，那么把前面的删除掉。
        if (uploadMax != null) {
            if (uploadMax > 0) {
                List<Upload> list = list(ownerType, ownerId, bizType);
                for (int i = uploadMax - 1; i < list.size(); i++) {
                    Upload upload = list.get(i);
                    this.delete(upload.getUploadId());
                }
            }
        }

        //newFileName = newFileName + extName;

        //处理文件数据
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;

        String extName = FileUtil.getExtName(originalFilename);
        File newFile = FileUtil.newFile(extName);
        try {
            byte[] buffer = file.getBytes();
            fos = new FileOutputStream(newFile);
            bos = new BufferedOutputStream(fos);
            bos.write(buffer);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //备份一份原图
        File backFile = FileUtil.getBackupFile(newFile);
        FileUtils.copyFile(newFile, backFile);

        String uploadRoot = ApplicationContext.getInstance().getUploadRoot();
        Upload upload = this.uploadDao.get(uploadId);
        upload.setAbsolutePath(newFile.getAbsolutePath().substring(uploadRoot.length() - "/upload".length()));
        upload = update(upload);
        return upload;
    }




    public Upload createUpload(File file, String originalFilename, String ownerType, String ownerId, String bizType, Integer seq) {
        //App app = ApplicationContext.getInstance().getApp();
        String uploadRoot = ApplicationContext.getInstance().getUploadRoot();

        String absolutePath = file.getAbsolutePath().substring(uploadRoot.length() - "/upload".length());
        absolutePath = absolutePath.replaceAll("\\\\", "/");

        Upload upload = new Upload();
        upload.setAbsolutePath(absolutePath);
        upload.setNewFileName(file.getName());
        upload.setFileName(originalFilename);
        upload.setUploadTime(new Date());
        upload.setType(FileUtil.getExtName(file.getName()));
        upload.setDeleteFlag("N");
        upload.setOwnerType(ownerType);
        upload.setOwnerId(ownerId);
        upload.setBizType(bizType);
        upload.setSeq(seq);

        upload.setSize(file.length());
        upload = uploadDao.create(upload);
        return upload;
    }

    public List<Upload> list(String ownerType, String ownerId, String bizType) {
        return this.uploadDao.list(ownerType, ownerId, bizType);
    }

    public Upload getUpload(String ownerType, String ownerId, String bizType) {
        List<Upload> list = list(ownerType, ownerId, bizType);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public List<Upload> saveUpload(String ownerType, String newId) {
        Operator o = ApplicationContext.getInstance().getOperator();
        List<Upload> list = list(ownerType, o.getOperatorId(), null);
        for (Upload fb : list) {
            fb.setOwnerId(newId);
            this.uploadDao.update(fb);
        }
        return list;
    }

    public void saveUpload(String ownerType, String ownerId, String newId) {
        List<Upload> list = list(ownerType, ownerId, null);
        for (Upload fb : list) {
            fb.setOwnerId(newId);
            this.uploadDao.update(fb);
        }
    }

    public Upload delete(String uploadId) throws IOException {
        Upload upload = uploadDao.get(uploadId);

        String absolutePath = upload.getAbsolutePath();

        File file = new File(ApplicationContext.getInstance().getUploadRoot() + absolutePath);
        if (file.exists()) {
            file.delete();
        }

        uploadDao.delete(uploadId);
        return upload;
    }

    public Upload update(Upload upload) {
        return this.uploadDao.merge(upload);
    }

    public void deleteByMasterId(String ownerId, String ownerType, String bizType) {
        Finder f = new Finder("Upload");
        if (StringUtils.isBlank(ownerId)) {
            throw new AppException("E-PUB-02", "ownerId 不能为空。");
        } else {
            f.equal("ownerId", ownerId);
        }
        if (StringUtils.isBlank(ownerType)) {
            throw new AppException("E-PUB-03", "ownerType 不能为空。");
        } else {
            f.equal("ownerType", ownerType);
        }
        f.equal("bizType", bizType);

        List list = this.uploadDao.find(f);
    }

    /**
     * CKEDITOR控件上传图片
     *
     * @param file
     * @param filePath
     */
    public void uploadImage(MultipartFile file, String filePath) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File newFile = new File(dir, file.getOriginalFilename());
            byte[] buffer = file.getBytes();
            fos = new FileOutputStream(newFile);
            bos = new BufferedOutputStream(fos);
            bos.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void download(String path, HttpServletResponse response) {
        try {
            //创建file对象
            File file = new File(path);
            if (file.isDirectory()) {//下载的是一个文件夹
                String random = RandomStringUtils.random(4, false, true);
                String newFileName = new SimpleDateFormat("HHmmss").format(new Date()) + random;
                String realPath = ApplicationContext.getInstance().getUploadRoot() + "/downloadZip/" + newFileName + ".zip";
                new ZipUtil().createZip(realPath, new File(path));
                file = new File(realPath);
            }
            //设置response的编码方式
            response.setContentType("application/x-msdownload");
            //写明要下载的文件的大小
            response.setContentLength((int) file.length());

            String fielName = path.substring(path.lastIndexOf("/") + 1);

            //设置附加文件名
            String srcFileName = getSrcFileName(fielName);
            if (StringUtils.isNotBlank(srcFileName)) {
                fielName = srcFileName;
            }

            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fielName, "UTF-8"));

            //读出文件到i/o流
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream buff = new BufferedInputStream(fis);

            byte[] b = new byte[1024];//相当于我们的缓存
            long k = 0;//该值用于计算当前实际下载了多少字节
            //从response对象中得到输出流,准备下载
            OutputStream myout = response.getOutputStream();
            //开始循环下载
            while (k < file.length()) {
                int j = buff.read(b, 0, 1024);
                k += j;
                //将b中的数据写到客户端的内存
                myout.write(b, 0, j);
            }
            //将写入到客户端的内存的数据,刷新到磁盘
            myout.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSrcFileName(String name) {
        Finder f = new Finder("Upload");
        f.equal("newFileName", name);
        List<Upload> up = this.uploadDao.find(f);
        if (up.size() > 0) {
            return up.get(0).getFileName();
        }
        return "";
    }

    public Upload view(String uploadId) {
        return uploadDao.get(uploadId);
    }

    public Upload getByPath(String path) {
        String sql = "SELECT * FROM cms_upload WHERE absolute_path=? ORDER BY create_time DESC";
        List<Upload> load = this.uploadDao.createSQLQuery(sql, path).addEntity(Upload.class).list();
        Upload upload = null;
        if (load.size() > 0) {
            upload = load.get(0);
        }
        return upload;
    }

    public Upload copy(Upload u, String ownerId, String bizType, String ownerType) {
        Upload upload = new Upload();
        upload.setOwnerId(ownerId);
        upload.setAbsolutePath(u.getAbsolutePath());
        upload.setBizType(bizType);
        upload.setOwnerType(ownerType);
        upload.setDeleteFlag("N");
        upload.setType(u.getType());
        upload.setFileName(u.getFileName());
        upload.setNewFileName(u.getNewFileName());
        upload.setSize(u.getSize());
        upload.setUploadTime(u.getUploadTime());
        this.uploadDao.create(upload);
        return upload;
    }

    public Upload getById(String uploadId) {
        return uploadDao.get(uploadId);
    }

    public void merge(Upload upload) {
        uploadDao.merge(upload);
    }

    public Upload getUploadBySeq(List<Upload> uploadList, int i) {
        for (Upload upload : uploadList) {
            if (upload.getSeq() == i) {
                return upload;
            }
        }
        return null;
    }

    public Upload getByOwnerId(String ownerId) {
        Finder f = new Finder("Upload");
        f.equal("ownerId", ownerId);
        Upload upload = this.uploadDao.getUnique(f);
        return upload;
    }
}