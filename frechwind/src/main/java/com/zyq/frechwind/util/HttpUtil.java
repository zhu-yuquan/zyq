package com.zyq.frechwind.util;

import com.zyq.frechwind.base.YmlConfig;
import com.zyq.frechwind.pub.bean.FileUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-04-29.
 */
public class HttpUtil {

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url 发送请求的URL
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url) throws IOException {
        HttpClient client = HttpClients.createDefault();

        HttpGet get = new HttpGet(url);

        HttpResponse response = client.execute(get);
        String result = EntityUtils.toString(response.getEntity(), "UTF-8");

        return result;
    }

    /**
     * 读取response返回的图片,并生成新文件
     *
     * @param url  请求url
     * @param path 图片保存路径
     * @return 是否成功
     */
    public static boolean sendGetReadImg(String url, String path) {

        HttpClient client = HttpClients.createDefault();

        boolean flag = false;
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = client.execute(get);

            File storeFile = new File(path);
            FileOutputStream output = new FileOutputStream(storeFile);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                byte b[] = new byte[1024];
                int j = 0;
                while ((j = instream.read(b)) != -1) {
                    output.write(b, 0, j);
                }
                output.flush();
                output.close();
                flag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static String sendPost(String url, Map<String, String> params) {
        return sendPost(url, params, new HashMap<String, String>());
    }


    public static String sendPost(String url, String jsonParam) {
        return sendPost(url, jsonParam, new HashMap<String, String>());
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url        发送请求的 URL
     * @param jsonParams 请求参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String jsonParams, Map<String, String> headers) {
        HttpClient client = HttpClients.createDefault();

        String result = null;
        try {
            HttpPost post = new HttpPost(url);
            for (String headerName : headers.keySet()) {
                post.setHeader(headerName, headers.get(headerName));
            }

            StringEntity entity = new StringEntity(jsonParams, "UTF-8");
            entity.setContentType("application/json");
            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url    发送请求的 URL
     * @param params 请求参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, String> params, Map<String, String> headers) {
        HttpClient client = HttpClients.createDefault();

        String result = null;
        try {
            HttpPost post = new HttpPost(url);
            for (String headerName : headers.keySet()) {
                post.setHeader(headerName, headers.get(headerName));
            }

            if (null != params) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (String name : params.keySet()) {
                    nvps.add(new BasicNameValuePair(name, params.get(name)));
                }
                post.setEntity(new UrlEncodedFormEntity(nvps));
            }

            HttpResponse response = client.execute(post);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String post(String requestUrl, String accessToken, String params) throws Exception {
        String generalUrl = requestUrl + "?access_token=" + accessToken;
        URL url = new URL(generalUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(params);
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : headers.keySet()) {
            System.out.println(key + "--->" + headers.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        if (requestUrl.contains("nlp"))
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));
        else
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        System.out.println("result:" + result);
        return result;
    }

    //ssl方法
    public static String ssl(String url, String data, String mchId) {
        StringBuffer message = new StringBuffer();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream instream = null;
            if (YmlConfig.isDevEnv()) {
                instream = new FileInputStream(new File("D:/cert/apiclient_cert.p12"));
            } else {
                instream = new FileInputStream(new File("/root/apiclient_cert.p12"));
            }
            keyStore.load(instream, mchId.toCharArray());

            // 证书
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, mchId.toCharArray()).build();
            // 只允许TLSv1协议
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            //创建基于证书的httpClient,后面要用到
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            HttpPost httpost = new HttpPost(url);//接口

            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            //System.out.println("executing request" + httpost.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();

                //System.out.println("----------------------------------------");
                //System.out.println(response.getStatusLine());
                if (entity != null) {
                    //System.out.println("Response content length: " + entity.getContentLength());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        message.append(text);
                    }
                }
                EntityUtils.consume(entity);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return message.toString();
    }

    public static File downloadFile(String url, String extName) {
        HttpClient httpClient = HttpClients.createDefault();
        OutputStream out = null;
        InputStream in = null;

        File file = FileUtil.newFile(extName);
        try {
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            in = entity.getContent();

            long length = entity.getContentLength();
            if (length <= 0) {
                System.out.println("下载文件不存在！");
                return null;
            }

            System.out.println("The response value of token:" + httpResponse.getFirstHeader("token"));


            if (!file.exists()) {
                file.createNewFile();
            }

            out = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int readLength = 0;
            while ((readLength = in.read(buffer)) > 0) {
                byte[] bytes = new byte[readLength];
                System.arraycopy(buffer, 0, bytes, 0, readLength);
                out.write(bytes);
            }

            out.flush();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }
        for(Cookie cookie: cookies){
            if(cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
