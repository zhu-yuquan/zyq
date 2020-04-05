package com.zyq.frechwind.pub.dao;

import com.zyq.frechwind.base.Dao;
import com.zyq.frechwind.base.Finder;
import com.zyq.frechwind.pub.bean.Upload;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UploadDao extends Dao<Upload,String> {
    public List<Upload> list(String ownerType, String ownerId, String bizType) {

        Finder f = new Finder("Upload");
        if (StringUtils.isBlank(ownerType)) {
            throw new RuntimeException("ownerType can't is null.");
        }
        if (StringUtils.isBlank(ownerId)) {
            throw new RuntimeException("ownerId can't is null.");
        }

        f.equal("ownerType", ownerType);
        f.equal("ownerId", ownerId);
        f.equal("bizType", bizType);
        f.equal("deleteFlag", "N");
        f.order("createTime", "desc");

        return find(f);
    }

}
