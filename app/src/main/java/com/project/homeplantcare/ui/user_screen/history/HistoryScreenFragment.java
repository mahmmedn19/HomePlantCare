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
import com.project.homeplantcare.data.utils.AuthUtils;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentHistoryScreenBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HistoryScreenFragment extends BaseFragment<FragmentHistoryScreenBinding> implements HistoryAdapter.HistoryInteractionListener {

    private HistoryViewModel viewModel;
    private HistoryAdapter adapter;
    private List<HistoryItem> historyList = new ArrayList<>();

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
        showBackButton(false);

        setupRecyclerView();
        fetchHistory();
    }

    private void setupRecyclerView() {
        adapter = new HistoryAdapter(historyList, this);
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
                historyList = result.getData();
                binding.progressBar.setVisibility(View.GONE);
                if (historyList != null && !historyList.isEmpty()) {
                    adapter.updateList(historyList);
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
            }
        });
    }

    @Override
    public void onHistoryItemClicked(HistoryItem plant) {
        navigateToPlantDetails(plant.getPlantId());
    }

    private void navigateToPlantDetails(String plantId) {
        Bundle bundle = new Bundle();
        bundle.putString("plantId", plantId);
        Navigation.findNavController(requireView()).navigate(R.id.action_historyScreenFragment_to_plantDetailsFragment2, bundle);
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

        DialogUtils.showConfirmationDialog(
                requireContext(),
                "Confirm Deletion",
                "Are you sure you want to delete this history item?",
                "Yes",
                "No",
                (dialog, which) -> viewModel.removeFromHistory(userId, item.getHistoryId()).observe(getViewLifecycleOwner(), result -> {
                    if (result.getStatus() == Result.Status.SUCCESS) {
                        showToast("Removed from history");
                        fetchHistory();
                    }
                })
        );
    }
}