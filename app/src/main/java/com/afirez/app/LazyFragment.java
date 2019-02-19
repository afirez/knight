package com.afirez.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LazyFragment extends Fragment {
    public static final String TAG = "LazyFragment";

    protected String name;

    private boolean isPageResume;

    public static LazyFragment newInstance(String name) {
        LazyFragment fragment = new LazyFragment();
        fragment.name = name;
        return fragment;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView view = new TextView(getContext());
        view.setText(name);
        return view;
    }

    @Override
    public void onResume() {
        Log.e(TAG, name + "==>onResume,isHidden=" + isHidden() + ",getUserVisibleHint=" + getUserVisibleHint());
        if (!isPageResume && !isHidden() && getUserVisibleHint()) {
            isPageResume = true;
            onPageResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e(TAG, name + "==>onPause,isHidden=" + isHidden() + ",getUserVisibleHint=" + getUserVisibleHint());
        if (isPageResume) {
            isPageResume = false;
            onPagePause();
        }
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.e(TAG, name + "==>onHiddenChanged,isHidden=" + hidden + ",getUserVisibleHint=" + getUserVisibleHint());
        if (hidden) {
            if (isPageResume && getUserVisibleHint()) {
                isPageResume = false;
                onPagePause();
            }
        } else {
            if (!isPageResume && getUserVisibleHint()) {
                isPageResume = true;
                onPageResume();
            }
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.e(TAG, name + "==>setUserVisibleHint,isHidden=" + isHidden() + ",getUserVisibleHint=" + isVisibleToUser);
        if (isVisibleToUser) {
            if (!isPageResume && !isHidden()) {
                isPageResume = true;
                onPageResume();
            }
        } else {
            if (isPageResume && !isHidden()) {
                isPageResume = false;
                onPagePause();
            }
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    protected void onPageResume() {
        Log.e(TAG, name + "==>onPageResume");
    }

    protected void onPagePause() {
        Log.e(TAG, name + "==>onPagePause");
    }
}
