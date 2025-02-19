package com.project.homeplantcare.ui.user_screen.history;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentHistoryScreenBinding;
import com.project.homeplantcare.data.models.HistoryItem;
import com.project.homeplantcare.ui.base.BaseFragment;

import java.util.ArrayList;

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
        showBackButton(false);

        // ✅ Initialize adapter before observing data
        adapter = new HistoryAdapter(new ArrayList<>(), this);
        binding.recyclerHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerHistory.setAdapter(adapter);

        // ✅ Observe LiveData to update UI when data changes
        viewModel.getHistoryList().observe(getViewLifecycleOwner(), historyItems -> {
            adapter.updateList(historyItems);
        });
    }

    @Override
    public void onDeleteClicked(HistoryItem item) {
        viewModel.removeHistory(item);
        Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHistoryItemClicked(HistoryItem item) {
        // Navigate to PlantDetailsFragment
        Navigation.findNavController(requireView()).navigate(R.id.action_historyScreenFragment_to_plantDetailsFragment2);
    }
}

