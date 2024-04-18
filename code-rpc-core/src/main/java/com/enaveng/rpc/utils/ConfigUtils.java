package com.enaveng.rpc.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.dialect.Props;
import cn.hutool.setting.yaml.YamlUtil;

import java.util.Map;

/**
 * 读取配置文件信息并返回配置对象RpcConfig
 * 需要具有灵活性 对于不同的前缀以及环境都适配
 */
public class ConfigUtils {

    private static final String YML_FILE_EXT = ".yml";
    private static final String YAML_FILE_EXT = ".yaml";
    private static final String PROPERTIES_FILE_EXT = ".properties";
    private static final String CONFIG_PREFIX_NAME = "application";

    /**
     * 加载配置对象
     *
     * @param tClass
     * @param preFix
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String preFix) {
        return loadConfig(tClass, preFix, "");
    }


    public static <T> T loadConfig(Class<T> tClass, String preFix, String environment) {
        //按顺序读取配置对象类型 properties、yml、yaml
        T t = loadApplicationConfig(tClass, preFix, environment);
        if (BeanUtil.isEmpty(t)) {
            T t1 = loadYmlConfig(tClass, preFix, environment);
            if (BeanUtil.isEmpty(t1)) {
                T t2 = loadYamlConfig(tClass, preFix, environment);
                return t2;
            }
            return t1;
        }
        return t;
    }


    /**
     * 加载.properties配置对象
     *
     * @param tClass      转换的对象
     * @param preFix      读取的前缀 例如 rpc.name 前缀即为 "rpc"
     * @param environment 环境
     * @param <T>
     * @return
     */
    public static <T> T loadApplicationConfig(Class<T> tClass, String preFix, String environment) {
        try {
            String configFile = buildConfigFile(CONFIG_PREFIX_NAME, environment, PROPERTIES_FILE_EXT);
            Props props = new Props(configFile);
            return props.toBean(tClass, preFix);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加载.Yaml配置对象
     *
     * @param tClass      转换的对象
     * @param preFix      读取的前缀 例如 rpc.name 前缀即为 "rpc"
     * @param environment 环境
     * @param <T>
     * @return
     */
    public static <T> T loadYamlConfig(Class<T> tClass, String preFix, String environment) {
        try {
            String configFile = buildConfigFile(CONFIG_PREFIX_NAME, environment, YAML_FILE_EXT);
            Map<String, Object> map = YamlUtil.loadByPath(configFile);
            JSONObject jsonObject = JSONUtil.parseObj(map).getJSONObject(preFix);
            return JSONUtil.toBean(jsonObject, tClass);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加载.Yml配置对象
     *
     * @param tClass      转换的对象
     * @param preFix      读取的前缀 例如 rpc.name 前缀即为 "rpc"
     * @param environment 环境
     * @param <T>
     * @return
     */
    public static <T> T loadYmlConfig(Class<T> tClass, String preFix, String environment) {
        try {
            String configFile = buildConfigFile(CONFIG_PREFIX_NAME, environment, YML_FILE_EXT);
            Map<String, Object> map = YamlUtil.loadByPath(configFile);
            //将map对象转换为JSONObject对象
            JSONObject rpcConfigProps = JSONUtil.parseObj(map).getJSONObject(preFix);
            return JSONUtil.toBean(rpcConfigProps, tClass);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 构建配置文件信息
     *
     * @param configPreFix 文件前缀 写死为application
     * @param environment  环境
     * @param configType   类型 具体包含三类 yml、yaml、properties
     * @return
     */
    public static String buildConfigFile(String configPreFix, String environment, String configType) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(configPreFix);
        if (StrUtil.isNotBlank(environment)) {
            stringBuilder.append("-").append(environment);
        }
        stringBuilder.append(configType);
        return stringBuilder.toString();
    }

    public static void main(String[] args) {


    }

}
