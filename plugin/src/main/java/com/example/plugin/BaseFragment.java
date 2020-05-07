package com.example.plugin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Wang.Wenhui
 * Date: 2020/5/7
 */
public abstract class BaseFragment extends Fragment {
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(layoutId(), container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    protected <T extends View> T findViewById(int viewId) {
        return mView.findViewById(viewId);
    }

    protected abstract void initView();

    protected abstract int layoutId();
}
