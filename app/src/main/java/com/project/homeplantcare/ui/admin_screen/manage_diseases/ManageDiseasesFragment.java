package com.project.homeplantcare.ui.admin_screen.manage_diseases;

import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentManageDiseasesBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageDiseasesFragment extends BaseFragment<FragmentManageDiseasesBinding> {


    @Override
    protected String getTAG() {
        return "ManageDiseasesFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_diseases;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Manage Diseases");
        showBackButton(false);

    }
}