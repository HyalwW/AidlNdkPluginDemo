package com.example.plugin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Wang.Wenhui
 * Date: 2020/5/7
 */
public interface PAInterface {
    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onCreate(Bundle savedInstanceState);

    void setProxy(AppCompatActivity proxyActivity);

}
