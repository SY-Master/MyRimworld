package com.symaster.mrd;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yinmiao
 * @since 2025/6/21
 */
public class MrAssetManager {

    private final AssetManager assetManager;
    private final Map<String, Map<String, String>> pathMap;
    private final Map<String, Map<String, Object>> objMap;

    public MrAssetManager() {
        this.assetManager = new AssetManager();
        this.pathMap = new HashMap<>();
        this.objMap = new HashMap<>();
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

                rtn.syncLoad(group, name, path, aClass);
            }
        }

        return rtn;
    }

    public boolean update() {
        return assetManager.update();
    }

    private void syncLoad(String group, String name, String path, Class<?> aClass) {
        try {
            Constructor<?> constructor = aClass.getConstructor(String.class);
            Object o = constructor.newInstance(path);
            objMap.computeIfAbsent(group, k -> new HashMap<>()).put(name, o);
        } catch (NoSuchMethodException |
                 InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void load(String group, String name, String path, Class<T> clazz) {
        pathMap.computeIfAbsent(group, k -> new HashMap<>()).put(name, path);

        if (assetManager.contains(path)) {
            return;
        }

        AssetLoaderParameters<T> param = new AssetLoaderParameters<>();
        param.loadedCallback = getLoadedCallback(group, name);

        assetManager.load(path, clazz, param);
    }

    private AssetLoaderParameters.LoadedCallback getLoadedCallback(String group, String name) {
        return (assetManager, fileName, type) -> objMap.computeIfAbsent(group, k -> new HashMap<>())
                                                       .put(name, assetManager.get(fileName, type));
    }

    public <T> T get(String group, String key, Class<T> clazz) {
        Object o = objMap.getOrDefault(group, Collections.emptyMap()).get(key);
        if (o == null) {
            throw new NullPointerException(String.format("指定资源没有维护：group:%s，key:%s", group, key));
        }

        return clazz.cast(o);
    }

    public <T> T getGlobal(String key, Class<T> clazz) {
        return get("global", key, clazz);
    }

}
