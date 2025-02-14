package com.project.homeplantcare.ui.user_screen.camera_screen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentCameraBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CameraFragment extends BaseFragment<FragmentCameraBinding> {


    @Override
    protected String getTAG() {
        return "CameraFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_camera;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("AI Camera");
        showBackButton(false);
        binding.btnAnalyze.setOnClickListener(
                v -> {
                    Navigation.findNavController(v).navigate(R.id.action_cameraFragment_to_plantDetailsFragment2);
                }
        );
    }
}