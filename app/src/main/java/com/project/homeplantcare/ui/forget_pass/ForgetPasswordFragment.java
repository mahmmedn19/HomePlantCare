package com.project.homeplantcare.ui.forget_pass;

import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentForgetPasswordBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ForgetPasswordFragment extends BaseFragment<FragmentForgetPasswordBinding> {


    @Override
    protected String getTAG() {
        return "ForgetPasswordFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_forget_password;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Forget Password");
        showBackButton(true);
    }
}