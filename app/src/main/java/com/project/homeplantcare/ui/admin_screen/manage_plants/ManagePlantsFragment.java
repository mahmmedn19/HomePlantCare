package com.project.homeplantcare.ui.admin_screen.manage_plants;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentManagePlantsBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.DialogUtils;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManagePlantsFragment extends BaseFragment<FragmentManagePlantsBinding>
        implements PlantsAdminAdapter.PlantAdminInteractionListener {

    private ManagePlantsViewModel viewModel;
    private PlantsAdminAdapter adapter;

    @Override
    protected String getTAG() {
        return "ManagePlantsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_plants;
    }

    @Override
    protected ViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Manage Plants");
        showBackButton(false);

        viewModel = new ViewModelProvider(this).get(ManagePlantsViewModel.class);

        setupRecyclerView();
        setupListeners();

        // Observe the loading, success, and error states
        viewModel.getAllPlants().observe(getViewLifecycleOwner(), result -> {
            switch (result.getStatus()) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE); // Show progress bar
                    binding.recyclerPlants.setVisibility(View.GONE); // Hide recycler
                    binding.placeholderImage.setVisibility(View.GONE); // Hide placeholder image
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE); // Hide progress bar
                    List<PlantItem> plantList = result.getData();
                    if (plantList != null && !plantList.isEmpty()) {
                        binding.recyclerPlants.setVisibility(View.VISIBLE); // Show recycler
                        binding.placeholderImage.setVisibility(View.GONE); // Hide placeholder image
                        adapter.notifyDataSetChanged(); // Update the list with new data
                    } else {
                        binding.recyclerPlants.setVisibility(View.GONE); // Hide recycler if empty
                        binding.placeholderImage.setVisibility(View.VISIBLE); // Show placeholder image
                    }
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE); // Hide progress bar
                    Toast.makeText(requireContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void setupRecyclerView() {
        viewModel.getAllPlants().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                List<PlantItem> plantList = result.getData();
                adapter = new PlantsAdminAdapter(plantList, this);
                binding.recyclerPlants.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.recyclerPlants.setAdapter(adapter);
            }
        });
    }

    private void setupListeners() {
        binding.fabAddPlant.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_nav_manage_plants_to_addPlaintAdminFragment);
        });
    }

    @Override
    public void onEditPlantClicked(PlantItem item) {
        // Navigate to Edit Plant with the plantId
        Bundle bundle = new Bundle();
        bundle.putString("plantId", item.getPlantId()); // Pass plantId as an argument
        Navigation.findNavController(requireView()).navigate(R.id.action_nav_manage_plants_to_addPlaintAdminFragment, bundle);
    }

    @Override
    public void onDeletePlantClicked(PlantItem item) {
        DialogUtils.showConfirmationDialog(requireContext(),
                "Delete Plant",
                "Are you sure you want to delete " + item.getName() + "?",
                "Delete",
                "Cancel",
                (dialog, which) -> {
                    viewModel.deletePlant(item); // Use plantId for deletion
                    adapter.notifyDataSetChanged(); // You might need to update the adapter data here.
                });
    }
}
