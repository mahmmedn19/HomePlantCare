package com.project.homeplantcare.ui.home_screen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentHomeBinding;
import com.project.homeplantcare.models.PlantItem;
import com.project.homeplantcare.models.QuickAccessItem;
import com.project.homeplantcare.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<FragmentHomeBinding>
        implements PlantAdapter.HomeInteractionListener, QuickAccessAdapter.HomeInteractionListener {

    private List<PlantItem> fakePlants;
    private List<QuickAccessItem> quickAccessItems;

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

        // Generate fake plant data and set up the PlantAdapter
        fakePlants = generateFakePlantData();
        setupPlantRecyclerView();

        // Generate Quick Access Items and set up the QuickAccessAdapter
        quickAccessItems = generateQuickAccessItems();
        setupQuickAccessRecyclerView();
    }

    // PlantAdapter interaction listeners
    @Override
    public void onFavoriteClicked(PlantItem item) {
        // Handle favorite click
    }

    @Override
    public void onCartClicked(PlantItem item) {
        // Handle cart click
        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_plantDetailsFragment);
    }

    // QuickAccessAdapter interaction listener
    @Override
    public void onCartClicked(QuickAccessItem item) {
        // Handle quick access item click
        switch (item.getLabel()) {
            case "Plants":
                // Navigate to Plants page
                break;
            case "Diseases":
                // Navigate to Diseases page
                break;
            case "Articles":
                // Navigate to Articles page
                break;
            case "Analyze Plant":
                // Navigate to Analyze Plant page
                break;
        }
    }

    // Generate fake plant data
    private List<PlantItem> generateFakePlantData() {
        List<PlantItem> plants = new ArrayList<>();
        plants.add(new PlantItem("Aloe Vera", "Great for skincare and easy to maintain.", 10.0, R.drawable.hoya4));
        plants.add(new PlantItem("Snake Plant", "Perfect for air purification.", 45.2, R.drawable.plant_logo));
        plants.add(new PlantItem("Money Plant", "Symbol of good fortune.",45.2, R.drawable.hoya4));
        return plants;
    }

    // Set up the PlantAdapter with horizontal scrolling
    private void setupPlantRecyclerView() {
        PlantAdapter plantAdapter = new PlantAdapter(fakePlants, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerPlantList.setLayoutManager(layoutManager);
        binding.recyclerPlantList.setAdapter(plantAdapter);
    }


    // Generate Quick Access Items
    private List<QuickAccessItem> generateQuickAccessItems() {
        List<QuickAccessItem> items = new ArrayList<>();
        items.add(new QuickAccessItem(R.drawable.ic_plant, "Plants"));
        items.add(new QuickAccessItem(R.drawable.ic_disease, "Diseases"));
        items.add(new QuickAccessItem(R.drawable.ic_articles, "Articles"));
        items.add(new QuickAccessItem(R.drawable.ic_analyze, "Analyze Plant"));
        return items;
    }

    // Set up the QuickAccessAdapter
    private void setupQuickAccessRecyclerView() {
        QuickAccessAdapter quickAccessAdapter = new QuickAccessAdapter(quickAccessItems, this);
        binding.recyclerQuickAccess.setLayoutManager(new GridLayoutManager(requireContext(), 2)); // 2 columns
        binding.recyclerQuickAccess.setAdapter(quickAccessAdapter);
    }
}
