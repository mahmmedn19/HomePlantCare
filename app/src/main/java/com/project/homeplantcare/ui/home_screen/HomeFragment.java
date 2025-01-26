package com.project.homeplantcare.ui.home_screen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentHomeBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<FragmentHomeBinding> {


    @Override
    protected String getTAG() {
        return "HomeFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_home;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();

    }
}