package com.zyq.frechwind.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 腾讯地图工具类
 */
public class TencentLocationUtils {

    private static Logger log = LoggerFactory.getLogger(TencentLocationUtils.class);

    private static final String KEY = "HAYBZ-LTBRW-JPZRE-OPTR4-P3FXE-F7BKQ";
    private static final String SECRET_KEY = "";

    /**
     * 通过经纬度获取位置
     * https://apis.map.qq.com/ws/geocoder/v1/?location=39.984154,116.307490&get_poi=1&key=HAYBZ-LTBRW-JPZRE-OPTR4-P3FXE-F7BKQ
     * @param lng 经度
     * @param lat 纬度
     * @return
     */
    public static List<String> getAidVByLocation(String lng, String lat) throws IOException {
        // 参数解释：lng：经度，lat：维度。KEY：腾讯地图key，get_poi：返回状态。1返回，0不返回

        String json = HttpUtil.sendGet("https://apis.map.qq.com/ws/geocoder/v1/?location=" + lat + "," + lng + "&get_poi=1&key=" + KEY);

        JsonParser parser = new JsonParser();
        // 2.获得 根节点元素
        JsonElement element = parser.parse(json);
        // 3.根据 文档判断根节点属于 什么类型的 Gson节点对象
        JsonObject root = element.getAsJsonObject();

        JsonObject addressComponent = root.getAsJsonObject("result").getAsJsonObject("addressComponent");

        List list = new ArrayList<>();

        //省
        String province = addressComponent.get("province").getAsString();
        list.add(province);

        //市
        String city = addressComponent.get("city").getAsString();
        list.add(city);

        //区
        String district = addressComponent.get("district").getAsString();
        list.add(district);

        //街道
        String street = addressComponent.get("street").getAsString();
        list.add(street);

        return list;
    }

    /**
     * api网址http://lbs.qq.com/webservice_v1/guide-geocoder.html
     * 地址转换坐标
     * 示例: https://apis.map.qq.com/ws/geocoder/v1/?address=北京市海淀区彩和坊路海淀西大街74号&key=HAYBZ-LTBRW-JPZRE-OPTR4-P3FXE-F7BKQ
     * @param address 详细地址
     * @return
     */
    public static Map<String, Float> geocoder(String address) throws IOException{
        String json = HttpUtil.sendGet("https://apis.map.qq.com/ws/geocoder/v1/?address=" + address + "&key=" + KEY);

        JsonParser parser = new JsonParser();
        // 2.获得 根节点元素
        JsonElement element = parser.parse(json);
        // 3.根据 文档判断根节点属于 什么类型的 Gson节点对象
        JsonObject root = element.getAsJsonObject();

        JsonObject addressComponent = root.getAsJsonObject("result").getAsJsonObject("location");

        // 经度
        Float lng = addressComponent.get("lng").getAsFloat();
        // 纬度
        Float lat = addressComponent.get("lat").getAsFloat();

        Map<String, Float> map = new HashMap<>();
        map.put("lng", lng);
        map.put("lat", lat);

        return map;
    }

}