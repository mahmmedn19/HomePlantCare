package com.project.homeplantcare.ui.articles_details;

import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentArticlesDetailsBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ArticlesDetailsFragment extends BaseFragment<FragmentArticlesDetailsBinding> {

    private ArticlesDetailsViewModel viewModel;

    @Override
    protected String getTAG() {
        return "ArticlesDetailsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_articles_details;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(ArticlesDetailsViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Article Details");
        showBackButton(true);

        // Get article ID passed from the previous fragment
        String articleId = getArguments() != null ? getArguments().getString("articleId") : null;

        if (articleId != null) {
            // Fetch article details based on the ID
            viewModel.fetchArticleDetails(articleId);
            observeArticleDetails();
        } else {
            showToast("Invalid Article ID");
        }
    }

    private void observeArticleDetails() {
        viewModel.getArticleDetails().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                binding.setArticle(result.getData());  // Bind article data to the layout
                String base64Image = result.getData().getImageResId(); // Make sure the article has imageResId
                if (base64Image != null && !base64Image.isEmpty()) {
                    Bitmap bitmap = ImageUtils.decodeBase64ToImage(base64Image);
                    binding.articleImage.setImageBitmap(bitmap);
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
