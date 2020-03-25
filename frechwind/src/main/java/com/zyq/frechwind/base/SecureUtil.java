package com.zyq.frechwind.base;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.owasp.validator.html.*;

@Service
public class SecureUtil {

    /**
     * 添加一条数据，计数加1
     * 如果有问题，那么会抛出异常
     *
     * @param content 如果内容中很多非法字符，那么也会判断有问题。
     */
    public static int hanziRate(String content) throws AppException {
        String realContent = getContentWithoutSign(content);
        return new Double(realContent.length() / content.length() * 100).intValue();
    }

    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * （1）过滤出字母的正则表达式     [^(A-Za-z)]
     * （2） 过滤出 数字 的正则表达式     [^(0-9)]
     * （3） 过滤出 中文 的正则表达式     [^(\\u4e00-\\u9fa5)]
     * （4） 过滤出字母、数字和中文的正则表达式     [^(a-zA-Z0-9\\u4e00-\\u9fa5)]
     *
     * @param content
     * @return
     */
    public static String getContentWithoutSign(String content) {
        String reg = "[^a-zA-Z0-9\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(content);
        content = mat.replaceAll("");
        return content;
    }

    public static void checkInvalidChar(HttpServletRequest request) {
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String[] values = request.getParameterValues(name);
        }
    }

    public static Set<String> sensitiveWordList(String sensitiveWordStr, String... content) {
        if (content == null) {
            return new HashSet<String>();
        }
        Set<String> noList = new HashSet<String>();

        String[] sensitiveWordArray = sensitiveWordStr.split(";");

        for (String c : content) {
            if (StringUtils.isBlank(c)) {
                continue;
            }
            //去掉特殊符号
            c = SecureUtil.getContentWithoutSign(c);
            //id字段不验证
            if (c.length() == 32 && StringUtils.isAlphanumeric(c)) {
                continue;
            }
            for (String sensitiveWord : sensitiveWordArray) {
                if (c.indexOf(sensitiveWord) != -1) {
                    noList.add(sensitiveWord);
                }
            }
        }
        return noList;
    }

    /**
     * 检查对象中，是否包含敏感词，如果包含，那么抛异常
     * 由于redis存在事务上的问题，所以这里暂时不用。
     *
     * @param o
     * @param sensitiveWordStr
     */
    public static void checkObjectSensitiveChar(Object o, String sensitiveWordStr) {
        try {
            Map<String, Object> map = PropertyUtils.describe(o);
            for (String propName : map.keySet()) {
                if (propName.endsWith("Id")) {
                    continue;
                }
                Object v = map.get(propName);
                if (v == null) {
                    continue;
                }
                if (v instanceof String) {
                    Set set = sensitiveWordList(sensitiveWordStr, (String) v);
                    if (set.size() > 0) {
                        AppException ae = new AppException("参数" + propName + "包含有非法字符，禁止访问。");
                        ae.setRemark(Arrays.toString(set.toArray()));
                        ae.setResponseCode(AppException.ERR_CODE__SensitiveError);
                        throw ae;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    /**
     * 检查参数中是否包含敏感词
     *
     * @param request
     * @param sensitiveWordStr
     */
    public static void checkSensitiveChar(HttpServletRequest request, String sensitiveWordStr) {
        String path = request.getServletPath();
        if (!path.equals("/pub-codeOption/update")) {
            Enumeration<String> names = request.getParameterNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                if (name.endsWith("Id") || name.equals("validKey")) {
                    continue;
                }
                String[] values = request.getParameterValues(name);
                Set set = sensitiveWordList(sensitiveWordStr, values);

                if (set.size() > 0) {
                    AppException ae = new AppException("参数" + name + "包含有非法字符，禁止访问。");
                    ae.setResponseCode(AppException.ERR_CODE__SensitiveError);
                    throw ae;
                }
            }
        }
    }

    /*
    public static String xssClean(String value) {
        AntiSamy antiSamy = new AntiSamy();
        try {
            File file = new File(SecureUtil.class.getClassLoader().getResource("antisamy-myspace.xml").getFile());
            Policy policy = Policy.getInstance(file);
            //CleanResults cr = antiSamy.scan(dirtyInput, policyFilePath);
            final CleanResults cr = antiSamy.scan(value, policy);
            //安全的HTML输出
            return cr.getCleanHTML();
        } catch (ScanException e) {
            e.printStackTrace();
        } catch (PolicyException e) {
            e.printStackTrace();
        }
        return value;
    }
    */

    /*
    public static ArrayList<String> errorHtmlMessages(String value) {
        AntiSamy antiSamy = new AntiSamy();
        try {
            File file = new File(SecureUtil.class.getClassLoader().getResource("antisamy-myspace.xml").getFile());
            Policy policy = Policy.getInstance(file);
            //CleanResults cr = antiSamy.scan(dirtyInput, policyFilePath);
            final CleanResults cr = antiSamy.scan(value, policy);
            //安全的HTML输出
            return cr.getErrorMessages();
        } catch (ScanException e) {
            throw new AppException(e);
        } catch (PolicyException e) {
            throw new AppException(e);
        }
    }
    */

    /**
     * 全角转半角
     *
     * @param string input string
     * @return the converted String
     */
    public static String full2Half(String string) {
        if (StringUtils.isBlank(string)) {
            return string;
        }

        char[] charArray = string.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == 12288) {
                charArray[i] = ' ';
            } else if (charArray[i] >= ' ' &&
                    charArray[i] <= 65374) {
                charArray[i] = (char) (charArray[i] - 65248);
            } else {

            }
        }


        return new String(charArray);
    }

    /**
     * 半角转全角
     *
     * @param value input value
     * @return converted value
     */
    public static String half2Full(String value) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        char[] cha = value.toCharArray();

        /**
         * full blank space is 12288, half blank space is 32
         * others :full is 65281-65374,and half is 33-126.
         */
        for (int i = 0; i < cha.length; i++) {
            if (cha[i] == 32) {
                cha[i] = (char) 12288;
            } else if (cha[i] < 127) {
                cha[i] = (char) (cha[i] + 65248);
            }
        }
        return new String(cha);
    }

    public static String passwordEncoder(String password) {
        if(StringUtils.isBlank(password)){
            throw new AppException("password can't is null.");
        }
        String key = "e70Qa01622E8651A9001961cB2a230X61D5a4d35e0085";
        String encodedPassword = DigestUtils.md5DigestAsHex((key + password).getBytes());
        return encodedPassword;
    }

    public static void main(String[] args) {
        System.out.println(passwordEncoder("123456"));
    }
}