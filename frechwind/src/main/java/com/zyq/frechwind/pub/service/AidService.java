package com.zyq.frechwind.pub.service;

import com.zyq.frechwind.base.AppException;
import com.zyq.frechwind.base.Finder;
import com.zyq.frechwind.pub.bean.Aid;
import com.zyq.frechwind.pub.dao.AidDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class AidService {

    @Autowired
    private AidDao aidDao;

    /**
     * 根据level查询所需要的 省、市、县、镇（乡）、村
     *
     * @param level level的值一般为：province、city、country、town、village
     * @return
     */
    public List<Aid> aidList(Aid.AidLevel level, String parent) {
        Finder finder = new Finder("Aid");
        finder.equal("level", level);
        finder.equal("parent", parent);
        List<Aid> aidList = aidDao.find(finder);
        if ("region".equals(level.getName())) {
            for (Aid aid : aidList) {
                List<Aid> childAidList = this.aidList(Aid.AidLevel.province, aid.getCode());
                aid.setChildAidList(childAidList);
            }
        }
        return aidList;
    }

    public List<Aid> departAidList(String searchText, String parent, String[] level) {
        Finder finder = new Finder("Aid");
        finder.in("level", level);
        finder.like("name", searchText);
        finder.hql("parent like '" + parent + "%'");
        return aidDao.find(finder);
    }

    /**
     * 根据level查询所需要的 省、市、县、镇（乡）、村
     *
     * @param parent
     * @return
     */
    public List<Aid> getByParentAidList(String parent) {
        Finder finder = new Finder("Aid");
        finder.equal("parent", parent);
        return aidDao.find(finder);
    }

    /**
     * 根据code查询区域
     *
     * @param code
     * @return
     */
    public Aid getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        Finder finder = new Finder("Aid");
        finder.equal("code", code);

        List<Aid> aidList = aidDao.find(finder);
        if (aidList.size() == 0) {
            return null;
        } else if (aidList.size() == 1) {
            return aidList.get(0);
        } else {
            throw new AppException("code:" + code + " 返回了多个对象。");
        }
    }

    public String getAid(String code) {
        String aidAddress = "";
        if (StringUtils.isNotBlank(code)) {

            Finder finder = new Finder("Aid");
            finder.equal("code", code);
            Aid aid = aidDao.getUnique(finder);
            //只根据aidcode显示
            aidAddress = aid.getName();
        }
        return aidAddress;
    }

    public Aid getByName(String cityName, String countyName) {
        Finder f = new Finder("Aid");
        f.equal("name", cityName);
        Aid aid = aidDao.getUnique(f);

        if (aid != null) {
            //市辖区特殊处理
            if ("province".equals(aid.getLevel().getName())) {
                Finder finder = new Finder("Aid");
                finder.equal("name", "市辖区");
                finder.equal("parent", aid.getCode());
                aid = aidDao.getUnique(finder);
            }

            Finder finder = new Finder("Aid");
            finder.equal("name", countyName);
            finder.equal("parent", aid.getCode());
            return aidDao.getUnique(finder);
        }
        return null;
    }

    public String getCompleteAddress(String province, String city, String country, String town, String village){
        StringBuilder completeAddress = new StringBuilder();
        if (StringUtils.isNotBlank(province)) {
            String name = getByCode(province).getName();
            if (StringUtils.isNotBlank(name)) {
                completeAddress.append(name);
            }
        }
        if (StringUtils.isNotBlank(city)) {
            String name = getByCode(city).getName();
            if (StringUtils.isNotBlank(name)) {
                completeAddress.append(name);
            }
        }
        if (StringUtils.isNotBlank(country)) {
            String name = getByCode(country).getName();
            if (StringUtils.isNotBlank(name)) {
                completeAddress.append(name);
            }
        }
        if (StringUtils.isNotBlank(town)) {
            String name = getByCode(town).getName();
            if (StringUtils.isNotBlank(name)) {
                completeAddress.append(name);
            }
        }
        if (StringUtils.isNotBlank(village)) {
            String name = getByCode(village).getName();
            if (StringUtils.isNotBlank(name)) {
                completeAddress.append(name);
            }
        }
        return completeAddress.toString();
    }


    public String getSimpleAddress(String country, String town, String village){
        StringBuilder simpleAddress = new StringBuilder();
        if (StringUtils.isNotBlank(country)) {
            String name = getByCode(country).getName();
            if (StringUtils.isNotBlank(name)) {
                simpleAddress.append(name);
            }
        }
        if (StringUtils.isNotBlank(town)) {
            String name = getByCode(town).getName();
            if (StringUtils.isNotBlank(name)) {
                simpleAddress.append(name);
            }
        }
        if (StringUtils.isNotBlank(village)) {
            String name = getByCode(village).getName();
            if (StringUtils.isNotBlank(name)) {
                simpleAddress.append(name);
            }
        }
        return simpleAddress.toString();
    }

    public String getDisplayName(String code) {
        String displayName = "";
        List<Aid> aidList = aidPath(code);
        for (Aid aid : aidList) {
            displayName += aid.getName();
        }

        return displayName;
    }

    public List<Aid> aidPath(String code) {
        List<Aid> aidList = new ArrayList<Aid>();
        Aid aid = getByCode(code);
        do {
            aidList.add(aid);
            aid = getByCode(aid.getParent());
        } while (
                aid != null && !aid.getParent().equals("0"));
        Collections.reverse(aidList);
        return aidList;
    }

    //返回下一个级别的名字
    public String getNextLevelName(String levelName) {
        List<String> levels = new ArrayList<>();
        levels.add("province");
        levels.add("city");
        levels.add("country");
        levels.add("town");
        levels.add("village");

        for (int i = 0; i < levels.size(); i++) {
            String level = levels.get(i);
            if (levelName.equals(level)) {
                if (i == 4) {
                    return null;
                }
                return levels.get(i + 1);
            }
        }
        return null;
    }

    public String getLevelByCode(String aidCode) {
        if (aidCode.endsWith("0000000000")) {
            return "province";
        } else if (aidCode.endsWith("00000000")) {
            return "city";
        } else if (aidCode.endsWith("000000")) {
            return "country";
        } else if (aidCode.endsWith("000")) {
            return "town";
        } else {
            return "village";
        }
    }
}