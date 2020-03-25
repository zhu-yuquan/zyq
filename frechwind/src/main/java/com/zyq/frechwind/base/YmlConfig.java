package com.zyq.frechwind.base;

import org.apache.commons.lang3.StringUtils;
import org.ho.yaml.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class YmlConfig {

    private Map map = new Hashtable();

    private static YmlConfig instance = new YmlConfig();

    private Logger log = LoggerFactory.getLogger(YmlConfig.class);

    private YmlConfig() {
    }

    public static void main(String[] args) {
        YmlConfig c = YmlConfig.getInstance();
        Object v = c.getConfigValue("spring.http.encoding.force");
        System.out.println(v);
    }

    private static YmlConfig getInstance() {
        return instance;
    }

    private synchronized void loadYml() {
        if (map.size() > 0) {
            return;
        }
        FileReader fileReader = null;
        try {
            String path = this.getClass().getClassLoader().getResource("application.yml").getFile();
            System.out.println(path);
            fileReader = new FileReader(path);
            //f = new FileReader("/mnt/application.yml");
        } catch (FileNotFoundException e) {
            throw new AppException(e.getMessage());
        }
        Map map = Yaml.loadType(fileReader, HashMap.class);
        this.map = map;
    }

    public static String value(String configName) {
        Object v = getInstance().getConfigValue(configName);
        if (v == null) {
            throw new AppException("can't find the config, name is " + configName);
        }
        return (String) v;
    }

    public static boolean useRedis() {
        return booleanValue("haolue.daoUseRedis");
    }

    public static boolean isPrdEnv() {
        return "prd".equalsIgnoreCase(YmlConfig.value("haolue.env"));
    }

    public static boolean isDevEnv() {
        return ("dev".equalsIgnoreCase(YmlConfig.value("haolue.env")));
    }

    public static boolean isUat() {
        return ("uat".equalsIgnoreCase(YmlConfig.value("haolue.env")));
    }

    public static boolean isAutoTestEnv() {
        return ("auto-test".equals(YmlConfig.value("haolue.env")));
    }

    public static boolean booleanValue(String configName) {
        Object v = getInstance().getConfigValue(configName);
        if (v == null) {
            throw new AppException("configName配置的值[" + v + "]无法转换成一个boolean。");
        }
        return (Boolean) v;
    }

    private Object getConfigValue(String configName) {
        if (map.size() == 0) {
            loadYml();
        }

        log.debug("configName:" + configName);
        String[] names = StringUtils.split(configName, ".");
        Stack<String> stack = new Stack();
        stack.addAll(Arrays.asList(names));
        Collections.reverse(stack);

        HashMap hm = new HashMap();
        hm.putAll(map);
        Object o = null;
        while (stack.size() > 0) {
            String name = stack.pop();
            o = hm.get(name);
            if (o == null || !(o instanceof HashMap)) {
                break;
            }
            hm = (HashMap) o;
        }
        return o;
    }
}