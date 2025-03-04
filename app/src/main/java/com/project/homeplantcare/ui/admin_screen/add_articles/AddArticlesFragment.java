package com.project.homeplantcare.ui.admin_screen.add_articles;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.databinding.FragmentAddArticlesBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.InputValidator;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddArticlesFragment extends BaseFragment<FragmentAddArticlesBinding> {

    private AddArticlesViewModel viewModel;
    private String articleId = null;
    private Bitmap selectedBitmap;

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
        viewModel = new ViewModelProvider(this).get(AddArticlesViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        articleId = getArguments() != null ? getArguments().getString("articleId") : null;

        if (articleId != null) {
            setToolbarTitle("Edit Article");
            binding.btnSaveArticle.setText("Update Article");
            viewModel.getArticleById(articleId);
        } else {
            setToolbarTitle("Add Article");
            binding.btnSaveArticle.setText("Save Article");
        }

        showBackButton(true);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupListeners();
        observeViewModel();

        if (articleId != null) {
            viewModel.getArticleItemLiveData().observe(getViewLifecycleOwner(), this::populateArticleData);
        }
    }

    private void setupListeners() {
        binding.etDate.setOnClickListener(v -> showDatePicker());
        binding.btnUploadImage.setOnClickListener(v -> openGallery());
        binding.btnSaveArticle.setOnClickListener(v -> {
            if (validateFields()) {
                if (articleId != null) {
                    viewModel.updateArticle(articleId, getArticleData());
                } else {
                    viewModel.addArticle(getArticleData());
                }
            } else {
                showToast("Please fill all required fields.");
            }
        });
    }

    private boolean validateFields() {
        boolean isValidTitle = InputValidator.validateField(binding.tilArticleTitle, Objects.requireNonNull(binding.etArticleTitle.getText()).toString(), "Title is required");
        boolean isValidContent = InputValidator.validateField(binding.tilContentPreview, Objects.requireNonNull(binding.etContentPreview.getText()).toString(), "Content preview is required");
        boolean isValidDate = InputValidator.validateField(binding.tilDate, Objects.requireNonNull(binding.etDate.getText()).toString(), "Date is required");

        return isValidTitle && isValidContent && isValidDate;
    }


    private ArticleItem getArticleData() {
        ArticleItem item = new ArticleItem();
        item.setTitle(Objects.requireNonNull(binding.etArticleTitle.getText()).toString().trim());
        item.setContentPreview(Objects.requireNonNull(binding.etContentPreview.getText()).toString().trim());
        item.setDate(Objects.requireNonNull(binding.etDate.getText()).toString().trim());

        // If a new image is selected, encode it
        if (selectedBitmap != null) {
            String encodedImage = ImageUtils.encodeImageToBase64(selectedBitmap);
            item.setImageResId(encodedImage);
        } else if (articleId != null) {
            // Preserve the existing image if updating without a new image
            ArticleItem existingArticle = viewModel.getArticleItemLiveData().getValue();
            if (existingArticle != null) {
                item.setImageResId(existingArticle.getImageResId());
            }
        }

        return item;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    binding.etDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                        binding.imgPreview.setImageBitmap(selectedBitmap);
                    } catch (IOException e) {
                        Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void observeViewModel() {
        viewModel.getIsArticleSaved().observe(getViewLifecycleOwner(), isSaved -> {
            if (isSaved) {
                showToast(articleId != null ? "Article updated successfully!" : "Article added successfully!");
                Navigation.findNavController(requireView()).navigateUp();
            }
        });
    }

    private void populateArticleData(ArticleItem article) {
        binding.etArticleTitle.setText(article.getTitle());
        binding.etContentPreview.setText(article.getContentPreview());
        binding.etDate.setText(article.getDate());

        if (article.getImageResId() != null && !article.getImageResId().isEmpty()) {
            Bitmap decodedBitmap = ImageUtils.decodeBase64ToImage(article.getImageResId());
            binding.imgPreview.setImageBitmap(decodedBitmap);
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}