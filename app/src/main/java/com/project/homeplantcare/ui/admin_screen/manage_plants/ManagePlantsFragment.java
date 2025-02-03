package com.project.homeplantcare.ui.admin_screen.manage_plants;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentManagePlantsBinding;
import com.project.homeplantcare.models.PlantItem;
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
    }

    private void setupRecyclerView() {
        List<PlantItem> plantList = viewModel.getMockPlants();
        adapter = new PlantsAdminAdapter(plantList, this);
        binding.recyclerPlants.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerPlants.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.fabAddPlant.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_nav_manage_plants_to_addPlaintAdminFragment);
        });
    }

    @Override
    public void onEditPlantClicked(PlantItem item) {
        Toast.makeText(getContext(), "Edit Plant: " + " " + item.getName(), Toast.LENGTH_SHORT).show();
        // Implement navigation to edit plant
    }

    @Override
    public void onDeletePlantClicked(PlantItem item) {
        DialogUtils.showConfirmationDialog(requireContext(),
                "Delete Plant",
                "Are you sure you want to delete " + item.getName() + "?",
                "Delete",
                "Cancel",
                (dialog, which) -> {
                    viewModel.deletePlant(item);
                    adapter.notifyDataSetChanged();
                });
        adapter.notifyDataSetChanged();
    }
}
