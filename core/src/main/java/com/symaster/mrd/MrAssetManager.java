package com.symaster.mrd;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.badlogic.gdx.assets.AssetManager;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yinmiao
 * @since 2025/6/21
 */
public class MrAssetManager {

    private final AssetManager assetManager;
    private final Map<String, Map<String, String>> map;

    public MrAssetManager() {
        this.assetManager = new AssetManager();
        this.map = new HashMap<>();
    }

    public static MrAssetManager create(JSONObject jsonObject) {
        MrAssetManager rtn = new MrAssetManager();

        for (String group : jsonObject.keySet()) {
            JSONArray items = jsonObject.getJSONArray(group);

            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);

                String type = item.getString("type");
                String name = item.getString("name");
                String path = item.getString("path");

                if (StringUtils.isBlank(type) || StringUtils.isBlank(name) || StringUtils.isBlank(path)) {
                    continue;
                }

                Class<?> aClass;
                try {
                    aClass = Class.forName(type);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                rtn.load(group, name, path, aClass);
            }
        }

        return rtn;
    }

    public void load(String group, String name, String path, Class<?> clazz) {
        map.computeIfAbsent(group, k -> new HashMap<>()).put(name, path);

        if (assetManager.contains(path)) {
            return;
        }

        assetManager.load(path, clazz);
    }

    public <T> T get(String group, String key, Class<T> clazz) {
        String path = map.getOrDefault(group, Collections.emptyMap()).get(key);

        if (path == null) {
            throw new NullPointerException(String.format("指定资源没有维护：group:%s，key:%s", group, key));
        }

        return assetManager.get(path, clazz);
    }

    public <T> T getGlobal(String key, Class<T> clazz) {
        return get("global", key, clazz);
    }

}
