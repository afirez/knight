package com.afirez.app;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleRegistry;

import java.util.List;

public class LazyFragment extends Fragment implements LifecycleObserver {

    private boolean isFragmentResumed;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getView() != null && !isFragmentResumed && !isHidden() && getUserVisibleHint()) {
            getView().post(new Runnable() {
                @Override
                public void run() {
                    dispatchResumed(true);
                }
            });
        }
    }

    @Override
    public void onResume() {
        if (!isFragmentResumed && !isHidden() && getUserVisibleHint()) {
            dispatchResumed(true);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (isFragmentResumed) {
            dispatchResumed(false);
        }
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            if (!isFragmentResumed && getUserVisibleHint()) {
                dispatchResumed(true);
            }
        } else {
            if (isFragmentResumed && getUserVisibleHint()) {
                dispatchResumed(false);
            }
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (!isFragmentResumed && !isHidden()) {
                dispatchResumed(true);
            }
        } else {
            if (isFragmentResumed && !isHidden()) {
                dispatchResumed(false);
            }
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    private void dispatchResumed(boolean resumed) {
        if (resumed && !isVisible()) {
            return;
        }

        if (resumed && isParentNotResumed()) {
            return;
        }

        if (isFragmentResumed == resumed) {
            return;
        }

        isFragmentResumed = resumed;

        if (isFragmentResumed) {
            handleEvent(Lifecycle.Event.ON_RESUME);
            onFragmentResume();
            childrenDispatchResumed(true);
        } else {
            handleEvent(Lifecycle.Event.ON_PAUSE);
            onFragmentPause();
            childrenDispatchResumed(false);
        }
    }

    public boolean isFragmentResumed() {
        return isFragmentResumed;
    }

    protected void onFragmentResume() {

    }

    protected void onFragmentPause() {

    }

    private boolean isParentNotResumed() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof LazyFragment) {
            LazyFragment parent = (LazyFragment) parentFragment;
            return !parent.isFragmentResumed();
        } else {
            return false;
        }
    }

    private void childrenDispatchResumed(boolean resumed) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        List<Fragment> fragments = childFragmentManager.getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof LazyFragment) {
                    ((LazyFragment) fragment).dispatchResumed(resumed);
                }
            }
        }
    }

    private void handleEvent(Lifecycle.Event event) {
        if (getLifecycle() instanceof LifecycleRegistry) {
            ((LifecycleRegistry) getLifecycle()).handleLifecycleEvent(event);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(this);
    }
}
