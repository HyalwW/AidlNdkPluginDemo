package com.example.aidlndkplugindemo.plugins;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plugin.PAInterface;

import java.lang.reflect.Method;

/**
 * Created by Wang.Wenhui
 * Date: 2020/5/7
 */
public class PluginProxyActivity extends AppCompatActivity {
    private static final String TAG = "PluginProxyActivity";
    protected AssetManager mAssetManager;
    protected Resources mResources;
    protected Resources.Theme mTheme;
    private ActivityInfo mActivityInfo;
    private PackageInfo packageInfo;
    private PAInterface PAInterface;
    private PluginManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //在替换资源前执行，不然报错
        getDelegate().onPostCreate(savedInstanceState);
        manager = PluginManager.getInstance(getBaseContext());
        loadResources(manager.getCurrentDexPath(), this);
        super.onCreate(savedInstanceState);
        String activityName = getIntent().getStringExtra("activity_name");
        PAInterface = manager.loadActivity(activityName);
        PAInterface.setProxy(this);
        PAInterface.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        PAInterface.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PAInterface.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PAInterface.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        PAInterface.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PAInterface.onDestroy();
    }


    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    @Override
    public Resources.Theme getTheme() {
        return mTheme == null ? super.getTheme() : mTheme;
    }

    protected void loadResources(String dexPath, Activity mProxyActivity) {
        initializeActivityInfo(dexPath);
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            Log.i("inject", "loadResource error:" + Log.getStackTraceString(e));
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        superRes.getDisplayMetrics();
        superRes.getConfiguration();

        if (mActivityInfo.theme > 0) {
            mProxyActivity.setTheme(mActivityInfo.theme);
        }
        Resources.Theme superTheme = mProxyActivity.getTheme();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(superTheme);
        try {
            mTheme.applyStyle(mActivityInfo.theme, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeActivityInfo(String dexPath) {
        packageInfo = getApplicationContext().getPackageManager().getPackageArchiveInfo(dexPath,
                PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        if ((packageInfo.activities != null) && (packageInfo.activities.length > 0)) {
//            if (mClass == null) {
//                mClass = packageInfo.activities[0].name;
//            }

            //Finals 修复主题BUG
            int defaultTheme = packageInfo.applicationInfo.theme;
            for (ActivityInfo a : packageInfo.activities) {
//                if (a.name.equals(mClass)) {
                mActivityInfo = a;
                // Finals ADD 修复主题没有配置的时候插件异常
                if (mActivityInfo.theme == 0) {
                    if (defaultTheme != 0) {
                        mActivityInfo.theme = defaultTheme;
                    } else {
                        if (Build.VERSION.SDK_INT >= 14) {
                            mActivityInfo.theme = android.R.style.Theme_DeviceDefault;
                        } else {
                            mActivityInfo.theme = android.R.style.Theme;
                        }
                    }
//                    }
                }
            }

        }
    }

}
