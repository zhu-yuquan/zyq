package com.zyq.frechwind.pub.rest;

import com.zyq.frechwind.base.SwaggerDoc;
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
@Api(description = "店铺配送区域和收货地址(aid地址表)", value = "rest-aid")
@RestController
@RequestMapping(value = "/rest-aid")
@SwaggerDoc
public class AidRest {
    @Autowired
    private AidService aidService;

    @ApiOperation(value = "区域列表", notes = "根据level和parent来获取区域列表")
    @RequestMapping(value = {"/aid-list"}, method = {RequestMethod.GET,RequestMethod.POST})
    public List<Aid> aidList(Aid.AidLevel level, String parent) {
        List<Aid> aidList = aidService.aidList(level, parent);
        return aidList;
    }

    @ApiOperation(value = "区域详细名称", notes = "根据code来获取区域名称")
    @RequestMapping(value = {"/aid-displayName"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String codeName(String code) {
        String displayName = aidService.getDisplayName(code);
        return displayName;
    }

    @ApiOperation(value = "区域", notes = "根据code来获取区域")
    @RequestMapping(value = {"/aid-code"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Aid getview(String code) {
        Aid aid = aidService.getByCode(code);
        return aid;
    }
}