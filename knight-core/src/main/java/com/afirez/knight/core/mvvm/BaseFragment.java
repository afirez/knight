package com.afirez.knight.core.mvvm;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.afirez.knight.core.LazyFragment;

import java.lang.reflect.ParameterizedType;

public abstract class BaseFragment<B extends ViewDataBinding, VM extends BaseViewModel>
        extends LazyFragment {

    protected B binding;

    protected VM viewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, layoutId(), container, false);
        viewModel = initViewModel();
        return binding.getRoot();
    }

    protected VM initViewModel() {
        Class<VM> vmClass = (Class<VM>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
        return ViewModelProviders.of(this).get(vmClass);
    }

    protected abstract int layoutId();

    @Override
    protected void onPageResume() {
        super.onPageResume();
        viewModel.onResume();
    }

    @Override
    protected void onPagePause() {
        super.onPagePause();
        viewModel.onPause();
    }
}
