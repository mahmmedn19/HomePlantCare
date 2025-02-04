package com.project.homeplantcare.ui.admin_screen.add_articles;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentAddArticlesBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddArticlesFragment extends BaseFragment<FragmentAddArticlesBinding> {

    private AddArticlesViewModel viewModel;

    @Override
    protected String getTAG() {
        return "AddArticlesFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_add_articles;
    }

    @Override
    protected ViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Add Article");
        showBackButton(true);

        viewModel = new ViewModelProvider(this).get(AddArticlesViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupListeners();
    }

    private void setupListeners() {
        binding.etDate.setOnClickListener(v -> showDatePicker());
        binding.btnUploadImage.setOnClickListener(v -> selectImage());
        binding.btnSaveArticle.setOnClickListener(v -> saveArticle());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    showToast("Selected Date: " + selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void selectImage() {
        // Placeholder function (Implement image picker logic)
        Uri mockUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.plant_2);
        showToast("Selected Image: " + mockUri.toString());
    }

    private void saveArticle() {
        viewModel.clearArticleData();
    }

    private void showToast(String message) {
        // Placeholder function (Implement toast message)
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
