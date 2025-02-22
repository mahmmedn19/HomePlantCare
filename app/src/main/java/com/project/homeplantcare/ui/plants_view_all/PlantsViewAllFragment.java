package com.project.homeplantcare.ui.plants_view_all;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentPlantsViewAllBinding;
import com.project.homeplantcare.ui.MainActivity;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.ui.home_screen.HomeViewModel;
import com.project.homeplantcare.ui.user_screen.UserMainActivity;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlantsViewAllFragment extends BaseFragment<FragmentPlantsViewAllBinding> implements ViewAllArticleAdapter.ViewAllInteractionListener {
    private List<ArticleItem> itemList;
    private HomeViewModel viewModel;

    @Override
    protected String getTAG() {
        return "PlantsViewAllFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_plants_view_all;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("All Articles");
        showBackButton(true);
        // Observe data loading for Articles
        observeArticlesData();
    }

    private void observeArticlesData() {
        viewModel.getAllArticles().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.progressBarArticles.setVisibility(View.VISIBLE);
                binding.recyclerNewArticles.setVisibility(View.GONE);
                binding.placeholderImageArticles.setVisibility(View.GONE);
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                binding.progressBarArticles.setVisibility(View.GONE);
                List<ArticleItem> articles = result.getData();
                if (articles != null && !articles.isEmpty()) {
                    binding.recyclerNewArticles.setVisibility(View.VISIBLE);
                    binding.placeholderImageArticles.setVisibility(View.GONE);
                    setupNewArticlesRecyclerView(articles);
                } else {
                    binding.recyclerNewArticles.setVisibility(View.GONE);
                    binding.placeholderImageArticles.setVisibility(View.VISIBLE);
                }
            } else {
                binding.progressBarArticles.setVisibility(View.GONE);
                binding.placeholderImageArticles.setVisibility(View.GONE);
                Toast.makeText(requireContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupNewArticlesRecyclerView(List<ArticleItem> articles) {
        ViewAllArticleAdapter articleAdapter = new ViewAllArticleAdapter(articles, this);
        binding.recyclerNewArticles.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerNewArticles.setAdapter(articleAdapter);
    }

    @Override
    public void onCartClicked(ArticleItem item) {
        // Handle cart click
        // Navigate to details page
        Bundle bundle = new Bundle();
        bundle.putString("articleId", item.getArticleId());
        if (isLogging()) {
            Navigation.findNavController(requireView()).navigate(R.id.action_plantsViewAllFragment2_to_articlesDetailsFragment2, bundle);
        } else {
            // User is not logged in
            Navigation.findNavController(requireView()).navigate(R.id.action_plantsViewAllFragment_to_articlesDetailsFragment, bundle);
        }
    }

    private boolean isLogging() {
        if (getActivity() instanceof UserMainActivity) {
            return true; // User is logged in
        } else if (getActivity() instanceof MainActivity) {
            return false; // User is not logged in
        }
        return false; // Default case
    }
}