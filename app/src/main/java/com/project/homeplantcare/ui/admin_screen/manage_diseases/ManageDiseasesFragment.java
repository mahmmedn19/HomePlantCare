package com.project.homeplantcare.ui.admin_screen.manage_diseases;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentManageDiseasesBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.DialogUtils;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageDiseasesFragment extends BaseFragment<FragmentManageDiseasesBinding> implements DiseaseItemAdapter.DiseaseInteractionListener {

    private DiseasesViewModel viewModel;
    private DiseaseItemAdapter adapter;

    @Override
    protected String getTAG() {
        return "ManageDiseasesFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_diseases;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(DiseasesViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Manage Diseases");
        showBackButton(false);

        setupRecyclerView();
        setupFab();

        // Observe the loading, success, and error states
        viewModel.getAllDiseases().observe(getViewLifecycleOwner(), result -> {
            switch (result.getStatus()) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE); // Show progress bar
                    binding.recyclerDiseases.setVisibility(View.GONE); // Hide recycler
                    binding.placeholderImage.setVisibility(View.GONE); // Hide placeholder image
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE); // Hide progress bar
                    List<DiseaseItem> itemList = result.getData();
                    if (itemList != null && !itemList.isEmpty()) {
                        binding.recyclerDiseases.setVisibility(View.VISIBLE); // Show recycler
                        binding.placeholderImage.setVisibility(View.GONE); // Hide placeholder image
                        adapter.notifyDataSetChanged();
                    } else {
                        binding.recyclerDiseases.setVisibility(View.GONE); // Hide recycler if empty
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

    private void setupFab() {
        binding.fabAddDisease.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_nav_manage_diseases_to_nav_add_disease);
        });
    }

    private void setupRecyclerView() {
        viewModel.getAllDiseases().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                List<DiseaseItem> itemList = result.getData();
                adapter = new DiseaseItemAdapter(itemList, this);
                binding.recyclerDiseases.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.recyclerDiseases.setAdapter(adapter);
            }
        });
    }



    @Override
    public void onEditDiseaseClicked(DiseaseItem item) {
        // Implement edit functionality or navigate to edit screen
        Bundle bundle = new Bundle();
        bundle.putString("diseaseId", item.getDiseaseId());
        Navigation.findNavController(requireView()).navigate(R.id.action_nav_manage_diseases_to_nav_add_disease, bundle);
    }

    @Override
    public void onDeleteDiseaseClicked(DiseaseItem disease) {
        DialogUtils.showConfirmationDialog(requireContext(),
                "Delete Disease",
                "Are you sure you want to delete " + disease.getName() + "?",
                "Delete",
                "Cancel",
                (dialog, which) -> {
                    viewModel.deleteDisease(disease); // Delete disease via ViewModel
                    showToast("Deleted: " + disease.getName());
                    adapter.notifyDataSetChanged(); // You might need to update the adapter data here.
                });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}

