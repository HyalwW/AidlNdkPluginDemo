package com.example.aidlndkplugindemo.plugins;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.plugin.PAInterface;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * Created by Wang.Wenhui
 * Date: 2020/5/7
 */
public class PluginManager {
    private HashMap<String, PluginHolder> plugins;
    private static Context base;
    private static PluginManager instance;
    private static PluginHolder currentHolder;

    public PluginManager(Context base) {
        PluginManager.base = base;
    }

    public static PluginManager getInstance(Context base) {
        synchronized (PluginManager.class) {
            if (instance == null) {
                instance = new PluginManager(base.getApplicationContext());
            }
        }
        return instance;
    }

    public boolean addPlugin(String pluginName, String dexPath) {
        if (plugins == null) {
            plugins = new HashMap<>();
        }
        for (String s : plugins.keySet()) {
            if (s.equals(pluginName)) {
                Log.e("wwh", "PluginManager --> addPlugin: falied; " + pluginName + " plugin has already exist!");
                return false;
            }
        }
        File dexDir = base.getDir("dex", 0);
        if (dexDir.exists()) {
            DexClassLoader dexClassLoader = new DexClassLoader(dexPath, dexDir.getAbsolutePath(), null, getClass().getClassLoader());
            plugins.put(pluginName, new PluginHolder(dexPath, dexClassLoader));
            return true;
        }
        return false;
    }

    public PluginManager apply(String pluginName) {
        currentHolder = null;
        if (plugins != null) {
            for (Map.Entry<String, PluginHolder> entry : plugins.entrySet()) {
                if (entry.getKey().equals(pluginName)) {
                    currentHolder = entry.getValue();
                }
            }
        }
        return instance;
    }

    public PAInterface loadActivity(String activityName) {
        checkLoader();
        try {
            //动态加载插件Activity
            Class<?> clazz = currentHolder.getClassLoader().loadClass(activityName);
            Constructor<?> localConstructor = clazz.getConstructor(new Class[]{});
            //拿到我们的activity 强转成它实现的接口PluginInterface
            return (PAInterface) localConstructor.newInstance(new Object[]{});
        } catch (Exception e) {
            return null;
        }
    }

    public String getCurrentDexPath() {
        checkLoader();
        return currentHolder.dexPath;
    }

    public void start(Context context, String activityName) {
        Intent intent = new Intent(context, PluginProxyActivity.class);
        intent.putExtra("activity_name", activityName);
        context.startActivity(intent);
    }

    private static class PluginHolder {

        private String dexPath;
        private DexClassLoader classLoader;

        public PluginHolder(String dexPath, DexClassLoader classLoader) {
            this.dexPath = dexPath;
            this.classLoader = classLoader;
        }

        public String getDexPath() {
            return dexPath;
        }

        public DexClassLoader getClassLoader() {
            return classLoader;
        }

    }

    private static void checkLoader() {
        if (currentHolder == null) {
            throw new IllegalStateException("u should call apply(plugin) before load");
        }
    }
}
