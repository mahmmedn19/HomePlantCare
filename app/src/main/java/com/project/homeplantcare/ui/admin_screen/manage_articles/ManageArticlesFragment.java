package com.project.homeplantcare.ui.admin_screen.manage_articles;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentManageArticlesBinding;
import com.project.homeplantcare.models.ArticleItem;
import com.project.homeplantcare.ui.base.BaseFragment;

import java.util.ArrayList;

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
        observeArticles();
    }

    private void setupFab() {
        binding.fabAddArticle.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_nav_manage_articles_to_nav_add_articles);
        });
    }

    private void setupRecyclerView() {
        binding.recyclerArticles.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ArticlesAdapter(new ArrayList<>(), this); // Start with empty list
        binding.recyclerArticles.setAdapter(adapter);
    }


    private void observeArticles() {
        viewModel.getArticlesLiveData().observe(getViewLifecycleOwner(), articles -> {
            if (articles != null && !articles.isEmpty()) {
                adapter.updateList(articles);
            }
        });
    }


    @Override
    public void onArticleClicked(ArticleItem article) {
        showToast("Article Clicked: " + article.getTitle());
    }

    @Override
    public void onEditArticleClicked(ArticleItem article) {
        showToast("Edit: " + article.getTitle());
    }

    @Override
    public void onDeleteArticleClicked(ArticleItem article) {
        viewModel.deleteArticle(article);
        showToast("Deleted: " + article.getTitle());
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
