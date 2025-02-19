package com.project.homeplantcare.ui.user_screen.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends ViewModel {

    private final MutableLiveData<List<HistoryItem>> historyList = new MutableLiveData<>(new ArrayList<>());

    public HistoryViewModel() {
        loadMockHistory();  // ✅ Load mock data when ViewModel is created
    }

    public LiveData<List<HistoryItem>> getHistoryList() {
        return historyList;
    }

    public void addHistory(HistoryItem item) {
        List<HistoryItem> currentList = historyList.getValue();
        if (currentList != null) {
            currentList.add(0, item);
            historyList.setValue(currentList);
        }
    }

    public void removeHistory(HistoryItem item) {
        List<HistoryItem> currentList = historyList.getValue();
        if (currentList != null) {
            currentList.remove(item);
            historyList.setValue(currentList);
        }
    }

    public void clearHistory() {
        historyList.setValue(new ArrayList<>());
    }

    public void loadMockHistory() {
        List<HistoryItem> mockHistory = new ArrayList<>();
        mockHistory.add(new HistoryItem("1", "101", "Aloe Vera", "Leaf Spot", "Feb 4, 2024", R.drawable.plant_2));
        mockHistory.add(new HistoryItem("2", "102", "Snake Plant", "Powdery Mildew", "Feb 3, 2024", R.drawable.plant_6));
        mockHistory.add(new HistoryItem("3", "103", "Money Plant", "Root Rot", "Feb 2, 2024", R.drawable.plant_7));

        historyList.setValue(mockHistory);
    }
}

