package com.enaveng.rpc.utils;


import cn.hutool.core.io.resource.InputStreamResource;
import cn.hutool.core.io.resource.ResourceUtil;
import com.enaveng.rpc.serializable.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spi加载器
 */
@Slf4j
public class SpiLoader {

    //定义资源类Map 具体为META-INF/rpc/system/*** 文件下的所有接口实现类  Map<String, Map<String, Class<?>>> ——> (接口名，(键名，实现类))
    private static Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();


    //对象实例缓存
    private static Map<String, Object> instanceCache = new ConcurrentHashMap<>();


    //系统Spi目录
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    //自定义Spi目录
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    //扫描路径
    private static final String[] SCAN_DIRS = new String[]{RPC_CUSTOM_SPI_DIR, RPC_SYSTEM_SPI_DIR};

    //实现类列表
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    //加载所有类型
    public static void loadAll() {
        log.info("加载所有SPI");
        for (Class<?> aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }

    /**
     * 加载某个类型
     * @param tClass
     * @param key
     * @return
     * @param <T>
     */
    public static <T> T getInstance(Class<?> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader 未加载 %s 类型", tClassName));
        }
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader 的 %s 不存在 key=%s 的类型", tClassName, key));
        }
        //获取想要加载的实现类型
        Class<?> implClass = keyClassMap.get(key);
        //从实例缓存中加载指定类型的实例 没有就将已经加载过的实例对象添加到缓存中
        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClass)) {
            try {
                instanceCache.put(implClassName, implClass.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return (T) instanceCache.get(implClassName);

    }


    /**
     * 加载某个类型
     *
     * @param loadClass  序列化接口的class对象
     * @return
     */
    public static Map<String, Class<?>> load(Class<?> loadClass) {
        log.info("加载类型为 {} 的 SPI", loadClass.getName());
        //扫描路径，用户自定义的SPI优先级高于系统SPI
        Map<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS) {
            //传递指定路径是必须用“/”进行分割 结尾可不添加“/”
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());
            //读取每一个资源文件
            for (URL resource : resources) {
                try {
                    //读取文件内容
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) { //按行读取内容
                        //文件当中的为 jdk=com.enaveng.rpc.serializable.JdkSerializer 需要以"="进行分割 得到的即为两部分字符串 示例为["jdk", "com.enaveng.rpc..."]
                        String[] strArray = line.split("=");
                        if (strArray.length > 1) {
                            String key = strArray[0];
                            String className = strArray[1];
                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (Exception e) {
                    log.error("spi resource load error", e);
                }
            }
        }
        loaderMap.put(loadClass.getName(), keyClassMap);
        return keyClassMap;
    }
}
