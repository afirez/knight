package com.afirez.app;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleRegistry;

public class LazyFragment extends Fragment implements LifecycleObserver {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(this);
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
        Log.w(TAG, name + "==>onResume,isHidden=" + isHidden() + ",getUserVisibleHint=" + getUserVisibleHint());
        if (!isPageResume && !isHidden() && getUserVisibleHint()) {
            isPageResume = true;
            onPageResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.w(TAG, name + "==>onPause,isHidden=" + isHidden() + ",getUserVisibleHint=" + getUserVisibleHint());
        if (isPageResume) {
            isPageResume = false;
            onPagePause();
        }
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.w(TAG, name + "==>onHiddenChanged,isHidden=" + hidden + ",getUserVisibleHint=" + getUserVisibleHint());
        if (hidden) {
            if (isPageResume && getUserVisibleHint()) {
                isPageResume = false;

                handleEvent(Lifecycle.Event.ON_PAUSE);

                onPagePause();
            }
        } else {
            if (!isPageResume && getUserVisibleHint()) {
                isPageResume = true;

                handleEvent(Lifecycle.Event.ON_RESUME);

                onPageResume();
            }
        }
        super.onHiddenChanged(hidden);
    }

    private void handleEvent(Lifecycle.Event event) {
        if (getLifecycle() instanceof LifecycleRegistry) {
            ((LifecycleRegistry) getLifecycle()).handleLifecycleEvent(event);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.w(TAG, name + "==>setUserVisibleHint,isHidden=" + isHidden() + ",getUserVisibleHint=" + isVisibleToUser);
        if (isVisibleToUser) {
            if (!isPageResume && !isHidden()) {
                isPageResume = true;

                handleEvent(Lifecycle.Event.ON_RESUME);

                onPageResume();
            }
        } else {
            if (isPageResume && !isHidden()) {
                isPageResume = false;

                handleEvent(Lifecycle.Event.ON_PAUSE);

                onPagePause();
            }
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    protected void onPageResume() {
        Log.w(TAG, name + "==>onPageResume");
    }

    protected void onPagePause() {
        Log.w(TAG, name + "==>onPagePause");
    }
}
