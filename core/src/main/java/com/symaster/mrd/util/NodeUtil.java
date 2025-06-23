package com.symaster.mrd.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.symaster.mrd.exception.EmptyException;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.game.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yinmiao
 * @since 2025/6/22
 */
public class NodeUtil {

    private static final Logger logger = LoggerFactory.getLogger(NodeUtil.class);

    public static  <T extends Node> T createNode(JSONObject json, Class<T> clazz) throws Exception {
        logger.info(json.toJSONString());

        String method = json.getString("method");
        String globalId = json.getString("globalId");
        String type = json.getString("type");

        /// 获取节点
        T node;
        if ("get".equals(method)) {
            if (StringUtils.isBlank(globalId)) {
                throw new EmptyException("globalId");
            }

            Node n = EntityManager.get(globalId);
            if (n == null) {
                throw new EmptyException("globalId -> Node");
            }

            if (StringUtils.isNotBlank(type)) {
                Class<?> aClass = Class.forName(type);
                if (!clazz.isAssignableFrom(aClass)) {
                    throw new EmptyException("globalId -> Node");
                }

                if (!aClass.isAssignableFrom(n.getClass())) {
                    throw new EmptyException("globalId -> Node");
                }
            } else {
                if (!clazz.isAssignableFrom(n.getClass())) {
                    throw new EmptyException("globalId -> Node");
                }
            }
            node = clazz.cast(n);

        } else {
            if (StringUtils.isBlank(type)) {
                throw new EmptyException("type");
            }
            Class<?> aClass = Class.forName(type);
            if (!clazz.isAssignableFrom(aClass)) {
                throw new EmptyException("type");
            }

            Object o;
            if (StringUtils.isNotBlank(globalId)) {
                Constructor<?> constructor = aClass.getConstructor(String.class);
                o = constructor.newInstance(globalId);
            } else {
                Constructor<?> constructor = aClass.getConstructor();
                o = constructor.newInstance();
            }

            node = clazz.cast(o);
        }

        /// 参数
        Set<String> continueKeys = new HashSet<>();
        continueKeys.add("method");
        continueKeys.add("globalId");
        continueKeys.add("type");
        continueKeys.add("nodes");
        for (String key : json.keySet()) {
            if (continueKeys.contains(key)) {
                continue;
            }

            String methodName = key.replaceAll("^[a-z_]", "set" + String.valueOf(key.toCharArray()[0]).toUpperCase());

            Method[] methods = node.getClass().getMethods();

            Method method2 = Arrays.stream(methods)
                                   .filter(e -> e.getName().equals(methodName))
                                   .findFirst()
                                   .orElse(null);

            if (method2 != null) {
                Parameter[] parameters = method2.getParameters();
                if (parameters != null && parameters.length > 1) {
                    Object[] args = new Object[parameters.length];

                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        Class<?> type1 = parameter.getType();

                        if (Node.class.isAssignableFrom(type1)) {
                            JSONObject jsonObject = json.getJSONObject(key);

                            Node node1 = createNode(jsonObject, Node.class);
                            args[i] = type1.cast(node1);
                        } else {
                            args[i] = json.getObject(key, type1);
                        }
                    }

                    method2.invoke(node, args);

                } else if (parameters != null && parameters.length == 1) {
                    Parameter parameter = parameters[0];
                    Class<?> type1 = parameter.getType();
                    if (Node.class.isAssignableFrom(type1)) {
                        JSONObject jsonObject = json.getJSONObject(key);
                        Node node1 = createNode(jsonObject, Node.class);
                        method2.invoke(node, type1.cast(node1));
                    } else {
                        method2.invoke(node, json.getObject(key, type1));
                    }
                }
            }
        }

        /// 子节点
        JSONArray nodes = json.getJSONArray("nodes");
        if (nodes != null && !nodes.isEmpty()) {
            for (int i = 0; i < nodes.size(); i++) {
                JSONObject jsonObject = nodes.getJSONObject(i);
                Node node1 = createNode(jsonObject, Node.class);
                node.add(node1);
            }
        }

        node.created();

        return node;
    }
}
