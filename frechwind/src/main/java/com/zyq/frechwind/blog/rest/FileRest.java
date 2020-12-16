package com.zyq.frechwind.blog.rest;

import com.zyq.frechwind.base.SwaggerDoc;
import com.zyq.frechwind.blog.service.FileNameUtilService;
import com.zyq.frechwind.pub.bean.Aid;
import com.zyq.frechwind.pub.service.AidService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by Administrator on 2017-04-23.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(description = "店铺配送区域和收货地址(aid地址表)", value = "rest-file")
@RestController
@RequestMapping(value = "/rest-file")
@SwaggerDoc
public class FileRest {

    @Autowired
    private FileNameUtilService fileNameUtilService;

    @ApiOperation(value = "区域列表", notes = "根据level和parent来获取区域列表")
    @GetMapping(value = {"/list"})
    public void fileLidt() {
        fileNameUtilService.getFileName(null);
    }


}