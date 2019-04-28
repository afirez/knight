package com.afirez.knight.core.mvvm;

import android.arch.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseViewModel extends ViewModel {

    private CompositeDisposable mDisposables = new CompositeDisposable();

    public BaseViewModel() {

    }

    public void addDisposable(Disposable disposable) {
        mDisposables.add(disposable);
    }

    public void onResume() {

    }

    public void onPause() {

    }

    @Override
    protected void onCleared() {
        if (!mDisposables.isDisposed()) {
            mDisposables.dispose();
        }
    }
}
