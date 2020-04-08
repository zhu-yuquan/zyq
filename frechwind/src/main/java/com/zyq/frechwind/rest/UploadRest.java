package com.zyq.frechwind.rest;

import com.zyq.frechwind.base.AppException;
import com.zyq.frechwind.pub.bean.FileUtil;
import com.zyq.frechwind.base.SwaggerDoc;
import com.zyq.frechwind.pub.bean.Upload;
import com.zyq.frechwind.pub.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/rest-cms-upload")
@SwaggerDoc
@Api(value = "/rest-cms-upload", description = "文件上传接口")
public class UploadRest {

    @Autowired
    private UploadService uploadService;

    @ApiOperation(value = "文件上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Map upload(@RequestBody MultipartFile uploadFile,
                      @RequestParam String ownerType,
                      @RequestParam String ownerId,
                      @RequestParam String bizType,
                      @RequestParam(required = false) String tip,
                      @RequestParam(required = false) Integer uploadMax,
                      @RequestParam(required = false) Integer imgMaxWidth,
                      @RequestParam(required = false) String waterMark,
                      @RequestParam(required = false) Integer seq,
                      HttpServletResponse response) throws IOException {
        if (uploadFile == null) {
            throw new AppException("E-PUB-01", "file is null");
        }
        if (uploadFile.isEmpty()) {
            throw new AppException("E-PUB-02", "file is empty");
        }
        String originalFilename = uploadFile.getOriginalFilename();
        long fileSize = uploadFile.getSize();
        if ("仅支持jpg、png、gif图片文件，且文件必须小于5M".equals(tip) && fileSize > (5 * 1024 * 1024)) {
            throw new AppException("文件过大");
        }
        String extName = FileUtil.getExtName(originalFilename);

        //上传图片
        Upload upload = uploadService.upload(uploadFile, originalFilename, ownerType, ownerId, bizType, uploadMax, seq);

        Map map = new HashMap();
        map.put("ownerType", ownerType);
        map.put("ownerId", ownerId);
        map.put("src", upload.getAbsolutePath());
        map.put("extName", extName);
        map.put("status", "success");
        response.setStatus(200);
        return map;
    }


    @ApiOperation(value = "文件删除")
    @RequestMapping(value = "/upload-delete", method = {RequestMethod.GET, RequestMethod.POST})
    public Map delete(@RequestParam String uploadId) throws IOException {
        Upload upload = uploadService.delete(uploadId);

        Map map = new HashMap();
        map.put("ownerType", upload.getOwnerType());
        map.put("ownerId", upload.getOwnerId());
        map.put("bizType", upload.getBizType());
        return map;
    }

    @ApiOperation(value = "文件改名")
    @RequestMapping(value = "/updateName", method = RequestMethod.POST)
    public String updateName(String uploadId, String fileOldName) {
        Upload upload = uploadService.view(uploadId);
        if (StringUtils.isNotBlank(fileOldName)) {
            upload.setFileName(fileOldName);
            uploadService.update(upload);
        }
        return "Success";
    }

    @ApiOperation(value = "文件列表")
    @RequestMapping(value = "/upload-list", method = RequestMethod.GET)
    public List<Upload> uploadList(String ownerType, String ownerId, String bizType) {
        List<Upload> uploadList = this.uploadService.list(ownerType, ownerId, bizType);
        return uploadList;
    }
}