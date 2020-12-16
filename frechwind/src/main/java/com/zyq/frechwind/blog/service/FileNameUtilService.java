package com.zyq.frechwind.blog.service;

import com.zyq.frechwind.pub.bean.FileUtil;
import com.zyq.frechwind.pub.bean.Upload;
import com.zyq.frechwind.pub.dao.UploadDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;

@Service
@Transactional
public class FileNameUtilService {
    @Autowired
    private UploadDao uploadDao;


    public void getFileName(String filePath) {
        filePath = "D:\\upload\\images";

        File file = new File(filePath);

        File[] fileList = file.listFiles();
        int i = 0;
        for (File fileN : fileList) {
            if (fileN.isFile()) {
                System.out.println("文件名:" + fileN.getName());
                System.out.println("文件路径:" + fileN.getPath().substring(2).replaceAll("\\\\", "/"));
                System.out.println("文件长度:" + fileN.length());

                i += 1;

                Upload upload = new Upload();
                upload.setAbsolutePath(fileN.getPath().substring(2).replaceAll("\\\\", "/"));
                upload.setNewFileName(fileN.getName());
                upload.setFileName(fileN.getName());
                upload.setUploadTime(new Date());
                upload.setType(FileUtil.getExtName(fileN.getName()));
                upload.setDeleteFlag("N");
                upload.setOwnerType("photo");
                upload.setOwnerId("1");
                upload.setBizType("JPG");
                upload.setSeq(0);

                upload.setSize(file.length());
                upload = uploadDao.create(upload);
            } else if (fileN.isDirectory()){
                getFileName(fileN.getPath());
            }
        }
        System.out.println("i=:" + i);

    }

}
