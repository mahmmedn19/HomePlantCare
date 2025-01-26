package com.project.homeplantcare.ui.base;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.BR;

public abstract class BaseFragment<VB extends ViewDataBinding> extends Fragment {

    protected abstract String getTAG();

    protected abstract int getLayoutIdFragment();

    protected abstract ViewModel getViewModel();

    protected VB binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutIdFragment(), container, false);

        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setVariable(BR.viewModel, getViewModel());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setup();
    }

    protected void setup() {
        // Default implementation, can be overridden in subclasses
    }

    protected void log(Object value) {
        Log.d(getTAG(), String.valueOf(value));
    }
}

