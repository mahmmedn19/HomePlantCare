package com.project.homeplantcare.ui.plants_view_all;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentPlantsViewAllBinding;
import com.project.homeplantcare.models.ArticleItem;
import com.project.homeplantcare.models.PlantItem;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.ui.home_screen.ArticleAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlantsViewAllFragment extends BaseFragment<FragmentPlantsViewAllBinding> implements ViewAllArticleAdapter.ViewAllInteractionListener {
    private List<ArticleItem> itemList;

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
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("All Articles");
        showBackButton(true);
        // Generate Article Items and set up ArticleAdapter
        itemList = generateFakeArticles();
        setupNewArticlesRecyclerView();

    }
    // Generate Fake Article Items
    private List<ArticleItem> generateFakeArticles() {
        List<ArticleItem> articles = new ArrayList<>();
        articles.add(new ArticleItem("How to Care for Indoor Plants", "Indoor plants improve air quality...", "Jan 20, 2024", R.drawable.plant_3));
        articles.add(new ArticleItem("Best Plants for Low Light", "Not all plants need bright sunlight...", "Feb 15, 2024", R.drawable.plant_4));
        articles.add(new ArticleItem("Watering Tips for Beginners", "Overwatering is a common mistake...", "Mar 5, 2024", R.drawable.plant_3));
        return articles;
    }
    // Set up the NewArticlesAdapter with horizontal scrolling
    private void setupNewArticlesRecyclerView() {
        ViewAllArticleAdapter articleAdapter = new ViewAllArticleAdapter(itemList, this);
        binding.recyclerNewArticles.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerNewArticles.setAdapter(articleAdapter);
    }
    @Override
    public void onCartClicked(ArticleItem item) {
        // Handle cart click
        // Navigate to details page
        Navigation.findNavController(requireView()).navigate(R.id.action_plantsViewAllFragment_to_articlesDetailsFragment);
    }
}