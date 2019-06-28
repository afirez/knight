package com.afirez.knight.core.mvvm;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.ParameterizedType;

public abstract class BaseActivity<B extends ViewDataBinding, VM extends BaseViewModel>
        extends AppCompatActivity {

    protected B binding;

    protected VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, layoutId());
        viewModel = initViewModel();
    }

    protected VM initViewModel() {
        Class<VM> vmClass = (Class<VM>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
        return ViewModelProviders.of(this).get(vmClass);
    }

    protected abstract int layoutId();

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.onPause();
    }
}
