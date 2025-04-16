package com.project.homeplantcare.ui.admin_screen.manage_articles;

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
import com.project.homeplantcare.databinding.FragmentManageArticlesBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.DialogUtils;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageArticlesFragment extends BaseFragment<FragmentManageArticlesBinding> implements ArticlesAdapter.ArticleInteractionListener {

    private ArticlesViewModel viewModel;
    private ArticlesAdapter adapter;

    @Override
    protected String getTAG() {
        return "ManageArticlesFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_articles;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(ArticlesViewModel.class);
        return viewModel;
    }


    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Manage Articles");
        showBackButton(false);
        setupFab();

        setupRecyclerView();

        // Observe the loading, success, and error states
        viewModel.getAllArticles().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.progressBar.setVisibility(View.VISIBLE); // Show progress bar
                binding.recyclerArticles.setVisibility(View.GONE); // Hide recycler
                binding.placeholderImage.setVisibility(View.GONE); // Hide placeholder image
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                binding.progressBar.setVisibility(View.GONE); // Hide progress bar
                List<ArticleItem> itemList = result.getData();
                if (itemList != null && !itemList.isEmpty()) {
                    binding.recyclerArticles.setVisibility(View.VISIBLE); // Show recycler
                    binding.placeholderImage.setVisibility(View.GONE); // Hide placeholder image
                    adapter.notifyDataSetChanged();
                } else {
                    binding.recyclerArticles.setVisibility(View.GONE); // Hide recycler if empty
                    binding.placeholderImage.setVisibility(View.VISIBLE); // Show placeholder image
                }
            } else if (result.getStatus() == Result.Status.ERROR) {
                binding.progressBar.setVisibility(View.GONE); // Hide progress bar
                Toast.makeText(requireContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupFab() {
        binding.fabAddArticle.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_nav_manage_articles_to_nav_add_articles);
        });
    }

    private void setupRecyclerView() {
        viewModel.getAllArticles().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                List<ArticleItem> itemList = result.getData();
                adapter = new ArticlesAdapter(itemList, this);
                binding.recyclerArticles.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.recyclerArticles.setAdapter(adapter);
            }
        });
    }


    @Override
    public void onArticleClicked(ArticleItem article) {
    }

    @Override
    public void onEditArticleClicked(ArticleItem article) {
        // implement edit article
        Bundle bundle = new Bundle();
        bundle.putString("articleId", article.getArticleId());
        Navigation.findNavController(requireView()).navigate(R.id.action_nav_manage_articles_to_nav_add_articles, bundle);
    }

    @Override
    public void onDeleteArticleClicked(ArticleItem article) {
        DialogUtils.showConfirmationDialog(
                requireContext(),
                "Delete Article",
                "Are you sure you want to delete this article?",
                "Delete",
                "Cancel",
                (dialog, which) -> {
                    viewModel.deleteArticle(article.getArticleId());
                    showToast("Article deleted successfully");
                    adapter.notifyDataSetChanged();
                },
                (dialog, which) -> {
                    dialog.dismiss();
                }
        );

    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
