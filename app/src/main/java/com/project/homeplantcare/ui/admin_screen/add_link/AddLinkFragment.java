package com.project.homeplantcare.ui.admin_screen.add_link;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentAddLinkBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddLinkFragment extends BaseFragment<FragmentAddLinkBinding> {

    private AddLinkViewModel viewModel;

    @Override
    protected String getTAG() {
        return "AddLinkFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_add_link;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(AddLinkViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Add Link");
        showBackButton(true);
        setupUI();
    }

    private void setupUI() {
        // Real-time validation while typing
        binding.etUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.url.setValue(s.toString());
                binding.urlInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Button Click Listener
        binding.btnAddLink.setOnClickListener(v -> {
            if (viewModel.validateUrl()) {
                Toast.makeText(getContext(), "URL is valid", Toast.LENGTH_SHORT).show();
            } else {
                binding.urlInputLayout.setError("Invalid URL format");
            }
        });
    }
}
