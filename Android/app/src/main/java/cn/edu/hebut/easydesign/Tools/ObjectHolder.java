package cn.edu.hebut.easydesign.Tools;

import java.util.HashMap;
import java.util.Map;

public class ObjectHolder {
    private static ObjectHolder holder;

    public static ObjectHolder getInstance() {
        if (holder == null) {
            synchronized (ObjectHolder.class) {
                if (holder == null) {
                    holder = new ObjectHolder();
                }
            }
        }
        return holder;
    }

    public Map<String, Object> objects = new HashMap<>();

    public void put(String key, Object data) {
        objects.put(key, data);
    }

    public <T> T get(String key) {
        return (T) objects.get(key);
    }

    public boolean has(String key) {
        return objects.containsKey(key);
    }
}
