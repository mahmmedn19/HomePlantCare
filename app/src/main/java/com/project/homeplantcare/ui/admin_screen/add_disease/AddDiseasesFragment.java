package com.project.homeplantcare.ui.admin_screen.add_disease;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentAddDiseasesBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddDiseasesFragment extends BaseFragment<FragmentAddDiseasesBinding> {

    private AddDiseasesViewModel viewModel;

    @Override
    protected String getTAG() {
        return "AddDiseasesFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_add_diseases;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(AddDiseasesViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();

        setToolbarVisibility(true);
        setToolbarTitle("Add Disease");
        showBackButton(true);

        binding.setLifecycleOwner(this);

        binding.btnSave.setOnClickListener(view -> {

        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
