package com.project.homeplantcare.ui.user_screen.history;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.models.HistoryItem;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.databinding.ItemHistoryBinding;
import com.project.homeplantcare.ui.base.BaseAdapter;

import java.util.List;

public class HistoryAdapter extends BaseAdapter<HistoryItem, ItemHistoryBinding> {

    private final HistoryInteractionListener listener;

    public HistoryAdapter(List<HistoryItem> itemList, HistoryInteractionListener listener) {
        super(itemList);
        this.listener = listener;
    }

    @Override
    public ItemHistoryBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemHistoryBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemHistoryBinding> holder, int position, HistoryItem currentItem) {
        ItemHistoryBinding binding = holder.binding;
        binding.setHistoryItem(currentItem);

        // Load plant image
        if (currentItem.getImageUrl() != null && !currentItem.getImageUrl().isEmpty()) {
            Bitmap bitmap = ImageUtils.decodeBase64ToImage(currentItem.getImageUrl());
            binding.imgPlant.setImageBitmap(bitmap);
        }

        // Set plant name
        binding.tvPlantName.setText(currentItem.getPlantName());

        // Set analysis date
        binding.tvAnalysisDate.setText(currentItem.getAnalysisDate());

        // Display diseases
        if (currentItem.getDiseaseName() != null && !currentItem.getDiseaseName().isEmpty()) {
            StringBuilder diseasesText = new StringBuilder();
            for (DiseaseItem disease : currentItem.getDiseaseName()) {
                diseasesText.append(disease.getName()).append(", ");
            }
            binding.tvDiseaseName.setText(diseasesText.substring(0, diseasesText.length() - 2));
        } else {
            binding.tvDiseaseName.setText("No disease detected");
        }

        binding.btnDelete.setOnClickListener(view -> listener.onDeleteClicked(currentItem));
        binding.layoutHistoryItem.setOnClickListener(view -> listener.onHistoryItemClicked(currentItem));

        binding.executePendingBindings();
    }


    public void updateList(List<HistoryItem> newList) {
        items.clear();
        items.addAll(newList);
        notifyDataSetChanged();
    }

    public interface HistoryInteractionListener {
        void onDeleteClicked(HistoryItem item);

        void onHistoryItemClicked(HistoryItem item);
    }
}
