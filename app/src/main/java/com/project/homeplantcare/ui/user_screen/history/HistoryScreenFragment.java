package com.project.homeplantcare.ui.user_screen.history;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.HistoryItem;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentHistoryScreenBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.AuthUtils;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HistoryScreenFragment extends BaseFragment<FragmentHistoryScreenBinding> implements HistoryAdapter.HistoryInteractionListener {

    private HistoryViewModel viewModel;
    private HistoryAdapter adapter;

    @Override
    protected String getTAG() {
        return "HistoryScreenFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_history_screen;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("History");
        showBackButton(true);

        setupRecyclerView();
        fetchHistory();
    }

    private void setupRecyclerView() {
        adapter = new HistoryAdapter(this);
        binding.recyclerHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerHistory.setAdapter(adapter);
    }

    private void fetchHistory() {
        String userId = AuthUtils.getCurrentUserId();
        if (userId == null) {
            showToast("Please log in to view history.");
            return;
        }

        viewModel.getHistory(userId).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.recyclerHistory.setVisibility(View.GONE);
                binding.noHistoryImage.setVisibility(View.GONE);
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                List<PlantItem> historyList = result.getData();
                binding.progressBar.setVisibility(View.GONE);
                if (historyList != null && !historyList.isEmpty()) {
                    adapter.submitList(historyList);
                    binding.recyclerHistory.setVisibility(View.VISIBLE);
                    binding.noHistoryImage.setVisibility(View.GONE);
                } else {
                    binding.recyclerHistory.setVisibility(View.GONE);
                    binding.noHistoryImage.setVisibility(View.VISIBLE);
                }
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerHistory.setVisibility(View.GONE);
                binding.noHistoryImage.setVisibility(View.VISIBLE);
                showToast("Failed to load history.");
            }
        });
    }

    @Override
    public void onHistoryItemClicked(PlantItem plant) {
        navigateToPlantDetails(plant.getPlantId());
    }

    private void navigateToPlantDetails(String plantId) {
        Bundle bundle = new Bundle();
        bundle.putString("plantId", plantId);
        Navigation.findNavController(requireView()).navigate(R.id.action_historyScreenFragment_to_plantDetailsFragment2);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClicked(HistoryItem item) {
        String userId = AuthUtils.getCurrentUserId();
        if (userId == null) {
            showToast("Please log in to delete history.");
            return;
        }

        viewModel.removeFromHistory(userId, item.getPlantId()).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                showToast("Removed from history");
                fetchHistory();
            } else {
                showToast("Failed to remove from history.");
            }
        });
    }

    @Override
    public void onHistoryItemClicked(HistoryItem item) {

    }
}