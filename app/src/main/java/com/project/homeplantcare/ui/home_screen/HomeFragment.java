package com.project.homeplantcare.ui.home_screen;

import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentHomeBinding;
import com.project.homeplantcare.models.ArticleItem;
import com.project.homeplantcare.models.CategoryItem;
import com.project.homeplantcare.models.PlantItem;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<FragmentHomeBinding>
        implements PlantAdapter.HomeInteractionListener, ArticleAdapter.HomeInteractionListener, CategoryAdapter.CategoryInteractionListener {

    private List<PlantItem> fakePlants;
    private List<ArticleItem> itemList;
    private List<CategoryItem> categoryItems;

    @Override
    protected String getTAG() {
        return "HomeFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_home;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(false);

        if (isLogging()) {
            binding.imgProfile.setVisibility(View.VISIBLE);
        } else {
            binding.imgProfile.setOnClickListener(view -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_loginFragment);
            });
        }

        binding.fabCamera.setOnClickListener(view -> {
            // check if user is logged in if not show dialog to login
            if (isLogging()) {
                Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_cameraFragment);
            } else {
                DialogUtils
                        .showConfirmationDialog(
                                requireContext(),
                                "Login Required",
                                "Please login to access this feature",
                                "Login",
                                "Cancel",
                                (dialogInterface, i) -> {
                                    Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_loginFragment);
                                },
                                null
                        );
            }

        });
        binding.tvViewAllArticles.setOnClickListener(view -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_plantsViewAllFragment);
        });

        // Generate fake plant data and set up PlantAdapter
        fakePlants = generateFakePlantData();
        setupPlantRecyclerView();

        // Generate Article Items and set up ArticleAdapter
        itemList = generateFakeArticles();
        setupNewArticlesRecyclerView();

        // Generate Category Items and set up CategoryAdapter
        categoryItems = generateFakeCategories();
        setupCategoryRecyclerView();
    }

    private List<PlantItem> generateFakePlantData() {
        List<PlantItem> plants = new ArrayList<>();

        plants.add(new PlantItem("Top", "Aloe Vera", R.drawable.plant_2));
        plants.add(new PlantItem("Indoor", "Snake Plant", R.drawable.plant_6));
        plants.add(new PlantItem("Garden", "Money Plant", R.drawable.plant_7));
        plants.add(new PlantItem("Outdoor", "Peace Lily", R.drawable.plant_7));
        plants.add(new PlantItem("Low Light", "Spider Plant", R.drawable.plant_2));

        return plants;
    }


    // Set up the PlantAdapter with horizontal scrolling
    private void setupPlantRecyclerView() {
        PlantAdapter plantAdapter = new PlantAdapter(fakePlants, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerPlantList.setLayoutManager(layoutManager);
        binding.recyclerPlantList.setAdapter(plantAdapter);
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
        ArticleAdapter articleAdapter = new ArticleAdapter(itemList, this);
        binding.recyclerNewArticles.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerNewArticles.setAdapter(articleAdapter);
    }

    // Generate Fake Category Items
    private List<CategoryItem> generateFakeCategories() {
        List<CategoryItem> categories = new ArrayList<>();
        categories.add(new CategoryItem("Top", true));
        categories.add(new CategoryItem("Outdoor", false));
        categories.add(new CategoryItem("Indoor", false));
        categories.add(new CategoryItem("Garden", false));
        return categories;
    }

    // Set up the CategoryAdapter with horizontal scrolling
    private void setupCategoryRecyclerView() {
        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryItems, this);
        binding.recyclerCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerCategories.setAdapter(categoryAdapter);
        // Set default category selection (First item selected)
        binding.recyclerCategories.post(() -> categoryAdapter.setDefaultCategory(0));
    }

    private boolean isLogging() {
        // Check if user is logged in
        return false;
    }

    @Override
    public void onCategoryClicked(CategoryItem category) {
        // Handle category selection (Filter plants accordingly)
    }

    @Override
    public void onFavoriteClicked(PlantItem item) {
        // Handle favorite click
        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_plantDetailsFragment);
    }

    @Override
    public void onCartClicked(PlantItem item) {
        // Handle cart click (Navigate to details page)
        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_plantDetailsFragment);
    }

    @Override
    public void onCartClicked(ArticleItem item) {
        // Handle article click
        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_articlesDetailsFragment);
    }
}
