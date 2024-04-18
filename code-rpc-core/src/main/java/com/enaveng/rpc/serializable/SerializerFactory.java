package com.enaveng.rpc.serializable;

import com.enaveng.rpc.utils.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器工厂
 */
public class SerializerFactory {
//    private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>() {
//        {
//            KEY_SERIALIZER_MAP.put(SerializerKeys.JDK, new JdkSerializer());
//            KEY_SERIALIZER_MAP.put(SerializerKeys.JSON, new JsonSerializer());
//            KEY_SERIALIZER_MAP.put(SerializerKeys.HESSIAN, new HessianSerializer());
//            KEY_SERIALIZER_MAP.put(SerializerKeys.KRYO, new KryoSerializer());
//        }
//    };

    static {
        SpiLoader.load(Serializer.class);
    }

    //默认的序列化器
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    //获取序列化器
    public static Serializer getInstance(String key) {
//        return KEY_SERIALIZER_MAP.getOrDefault(key, DEFAULT_SERIALIZER);
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
