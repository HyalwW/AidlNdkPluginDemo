package com.example.plugin;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Wang.Wenhui
 * Date: 2020/5/7
 */
public abstract class BaseActivity extends AppCompatActivity implements PAInterface {
    protected AppCompatActivity mProxyActivity;

    @Override
    public void setContentView(int layoutResID) {
        if (mProxyActivity != null) {
            mProxyActivity.setContentView(layoutResID);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (mProxyActivity == null) {
            return super.findViewById(id);
        } else {
            return mProxyActivity.findViewById(id);
        }
    }

    @Override
    public Context getApplicationContext() {
        return mProxyActivity == null ? super.getApplicationContext() : mProxyActivity.getApplicationContext();
    }

    @Override
    public Context getBaseContext() {
        return mProxyActivity == null ? super.getBaseContext() : mProxyActivity.getBaseContext();
    }

    @Override
    public Resources getResources() {
        return mProxyActivity == null ? super.getResources() : mProxyActivity.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return mProxyActivity == null ? super.getAssets() : mProxyActivity.getAssets();
    }

    @Override
    public Resources.Theme getTheme() {
        return mProxyActivity == null ? super.getTheme() : mProxyActivity.getTheme();
    }

    @Override
    public void onStart() {
        if (mProxyActivity == null) {
            super.onStart();
        }
    }

    @Override
    public void onResume() {
        if (mProxyActivity == null) {
            super.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mProxyActivity == null) {
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        if (mProxyActivity == null) {
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (mProxyActivity == null) {
            super.onDestroy();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (mProxyActivity == null) {
            super.onCreate(savedInstanceState);
        }
    }

    @Override
    public void setProxy(AppCompatActivity proxyActivity) {
        mProxyActivity = proxyActivity;
    }

    @Override
    public void finish() {
        if (mProxyActivity == null) {
            super.finish();
        } else {
            mProxyActivity.finish();
        }
    }

    @Override
    public FragmentManager getFragmentManager() {
        return mProxyActivity == null ? super.getFragmentManager() : mProxyActivity.getFragmentManager();
    }

    @Override
    public androidx.fragment.app.FragmentManager getSupportFragmentManager() {
        return mProxyActivity == null ? super.getSupportFragmentManager() : mProxyActivity.getSupportFragmentManager();
    }
}
