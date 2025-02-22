package com.project.homeplantcare.ui.home_screen;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.data.models.CategoryItem;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentHomeBinding;
import com.project.homeplantcare.ui.MainActivity;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.ui.user_screen.UserMainActivity;
import com.project.homeplantcare.utils.DialogUtils;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<FragmentHomeBinding>
        implements PlantAdapter.HomeInteractionListener, ArticleAdapter.HomeInteractionListener, CategoryAdapter.CategoryInteractionListener {

    private HomeViewModel viewModel;

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
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(false);

        if (isLogging()) {
            binding.imgProfile.setVisibility(View.GONE);
            binding.fabCamera.setVisibility(View.GONE);
        } else {
            binding.imgProfile.setVisibility(View.VISIBLE);
            binding.fabCamera.setVisibility(View.VISIBLE);
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
            if (isLogging()) {
                Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_plantsViewAllFragment2);
            } else {
                Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_plantsViewAllFragment);
            }
        });

        // Set up search listener
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No action required
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Trigger search filtering
                viewModel.filterPlants(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
        // Observe plants and filtered data
        observePlantsData();
        observeFilteredPlantsData();
        observeArticlesData();

        // Load the plants data when fragment is created
        viewModel.loadAllPlants();

    }

    private void observePlantsData() {
        viewModel.getAllPlants().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.progressBarPlants.setVisibility(View.VISIBLE);
                binding.recyclerPlantList.setVisibility(View.GONE);
                binding.placeholderImagePlants.setVisibility(View.GONE);
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                binding.progressBarPlants.setVisibility(View.GONE);
                List<PlantItem> plants = result.getData();

                if (plants != null && !plants.isEmpty()) {
                    binding.recyclerPlantList.setVisibility(View.VISIBLE);
                    binding.placeholderImagePlants.setVisibility(View.GONE);
                    setupPlantRecyclerView(plants);
                } else {
                    binding.recyclerPlantList.setVisibility(View.GONE);
                    binding.placeholderImagePlants.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), "No plants available", Toast.LENGTH_SHORT).show();
                }
            } else if (result.getStatus() == Result.Status.ERROR) {
                binding.progressBarPlants.setVisibility(View.GONE);
                binding.recyclerPlantList.setVisibility(View.GONE);
                binding.placeholderImagePlants.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeFilteredPlantsData() {
        viewModel.getFilteredPlants().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.progressBarPlants.setVisibility(View.VISIBLE);
                binding.recyclerPlantList.setVisibility(View.GONE);
                binding.placeholderImagePlants.setVisibility(View.GONE);
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                binding.progressBarPlants.setVisibility(View.GONE);
                List<PlantItem> filteredPlants = result.getData();

                if (filteredPlants != null && !filteredPlants.isEmpty()) {
                    binding.recyclerPlantList.setVisibility(View.VISIBLE);
                    binding.placeholderImagePlants.setVisibility(View.GONE);
                    setupPlantRecyclerView(filteredPlants);
                } else {
                    binding.recyclerPlantList.setVisibility(View.GONE);
                    binding.placeholderImagePlants.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), "No plants found matching the criteria", Toast.LENGTH_SHORT).show();
                }
            } else if (result.getStatus() == Result.Status.ERROR) {
                binding.progressBarPlants.setVisibility(View.GONE);
                binding.recyclerPlantList.setVisibility(View.GONE);
                binding.placeholderImagePlants.setVisibility(View.VISIBLE);
                String errorMessage = result.getErrorMessage();
                Toast.makeText(requireContext(), errorMessage != null ? errorMessage : "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void observeArticlesData() {
        viewModel.getAllArticles().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.progressBarArticles.setVisibility(View.VISIBLE);
                binding.recyclerNewArticles.setVisibility(View.GONE);
                binding.placeholderImageArticles.setVisibility(View.GONE);
            } else {
                binding.progressBarArticles.setVisibility(View.GONE);
                List<ArticleItem> articles = result.getData();

                if (result.getStatus() == Result.Status.SUCCESS && articles != null && !articles.isEmpty()) {
                    binding.recyclerNewArticles.setVisibility(View.VISIBLE);
                    binding.placeholderImageArticles.setVisibility(View.GONE);
                    setupNewArticlesRecyclerView(articles);
                } else {
                    binding.recyclerNewArticles.setVisibility(View.GONE);
                    binding.placeholderImageArticles.setVisibility(View.VISIBLE);

                    if (result.getStatus() == Result.Status.ERROR) {
                        Toast.makeText(requireContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void setupPlantRecyclerView(List<PlantItem> plants) {
        PlantAdapter plantAdapter = new PlantAdapter(plants, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerPlantList.setLayoutManager(layoutManager);
        binding.recyclerPlantList.setAdapter(plantAdapter);
    }

    private void setupNewArticlesRecyclerView(List<ArticleItem> articles) {
        ArticleAdapter articleAdapter = new ArticleAdapter(articles, this);
        binding.recyclerNewArticles.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerNewArticles.setAdapter(articleAdapter);
    }


    private boolean isLogging() {
        if (getActivity() instanceof UserMainActivity) {
            return true; // User is logged in
        } else if (getActivity() instanceof MainActivity) {
            return false; // User is not logged in
        }
        return false; // Default case
    }


    @Override
    public void onCategoryClicked(CategoryItem category) {
        // Handle category selection (Filter plants accordingly)
    }

    @Override
    public void onShowDetailsClicked(PlantItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("plantId", item.getPlantId());
        // Handle favorite click
        if (isLogging()) {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_plantDetailsFragment2,bundle);
        } else {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_plantDetailsFragment,bundle);
        }
    }

    @Override
    public void onPlantClicked(PlantItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("plantId", item.getPlantId());
        // Handle cart click (Navigate to details page)
        if (isLogging()) {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_plantDetailsFragment2,bundle);
        } else {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_plantDetailsFragment,bundle);
        }
    }

    @Override
    public void onArticleClicked(ArticleItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("articleId", item.getArticleId());
        // Handle article click
        if (isLogging()) {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_articlesDetailsFragment2,bundle);
        } else {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_articlesDetailsFragment,bundle);
        }
    }

}
