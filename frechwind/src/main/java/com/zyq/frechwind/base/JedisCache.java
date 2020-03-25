package com.zyq.frechwind.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;

public class JedisCache {
    private static Logger logger = LoggerFactory.getLogger(JedisCache.class);

//    private JedisPool pool;

    private static JedisCache cahce = new JedisCache();

    private JedisCache() {
//        JedisPoolConfig config = new JedisPoolConfig();
//        //最大空闲连接数, 应用自己评估，不要超过ApsaraDB for Redis每个实例最大的连接数
//        config.setMaxIdle(200);
//        //最大连接数, 应用自己评估，不要超过ApsaraDB for Redis每个实例最大的连接数
//        config.setMaxTotal(300);
//        config.setTestOnBorrow(false);
//        config.setTestOnReturn(false);
//        String host = "localhost";
//        String password = "密码";
//        //pool = new JedisPool(config, host, 6379, 3000, password);
//        pool = new JedisPool(config, host);
    }

    public static JedisCache getInstance() {
        return cahce;
    }

    public void setObject(String key, Object object) {
        /*if (StringUtils.isBlank(key)) {
            throw new AppException("key is null.");
        }
        if (object == null) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = pool.getResource();

            if(jedis == null){
                throw new AppException("jedis is null, redis server can unvaliable.");
            }
            Object o = getObject(key);
            if (o != null) {
                if (!o.getClass().equals(object.getClass())) {
                    String msg = "exists in redis." + o + "，the object with same key is" + object + "; key is：" + key;
                    try {
                        String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss");
                        File file = new File("/root/redis-cast-error.txt");
                        FileUtils.writeStringToFile(file, time, true);
                        FileUtils.writeStringToFile(file, msg, true);
                    } catch (IOException e) {
                        throw new AppException(e);
                    }
                    throw new AppException(msg);
                }
            }
            jedis.set(key.getBytes(), serialize(object));
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(e);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }*/
    }

    public Object getObject(String key)  {
//        Jedis jedis = null;
//        try {
//            jedis = pool.getResource();
//            if(jedis == null){
//                throw new AppException("redis 没有启动");
//            }
//            byte[] value = jedis.get(key.getBytes());
//            if (value == null) {
//                return null;
//            }
//            return unserialize(value);
//        }catch (Exception e){
//            throw new AppException(e);
//        }finally {
//            if(jedis != null) {
//                jedis.close();
//            }
//        }
        return null;
    }

    public void deleteObject(String key) {
//        Jedis jedis = null;
//        try {
//            jedis = pool.getResource();
//            jedis.del(key.getBytes());
//        } finally {
//            jedis.close();
//        }
    }

    private static byte[] serialize(Object object) throws IOException {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        // 序列化
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    private static Object unserialize(byte[] bytes)  {
        ByteArrayInputStream bais = null;
        // 反序列化
        try {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }catch (Exception e){
            //e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
    }
}