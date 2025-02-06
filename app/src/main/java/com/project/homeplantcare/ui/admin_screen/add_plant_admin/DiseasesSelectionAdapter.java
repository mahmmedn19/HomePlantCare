package com.project.homeplantcare.ui.admin_screen.add_plant_admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.homeplantcare.databinding.ItemDiseaseCheckboxBinding;
import com.project.homeplantcare.models.DiseaseItem;
import com.project.homeplantcare.ui.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DiseasesSelectionAdapter extends BaseAdapter<DiseaseItem, ItemDiseaseCheckboxBinding> {

    private final AddPlantViewModel viewModel;
    private final boolean isDialogAdapter;
    private List<DiseaseItem> fullList;

    public DiseasesSelectionAdapter(List<DiseaseItem> itemList, AddPlantViewModel viewModel, boolean isDialogAdapter) {
        super(itemList);
        this.viewModel = viewModel;
        this.isDialogAdapter = isDialogAdapter;
        this.fullList = new ArrayList<>(itemList);
    }

    @Override
    public ItemDiseaseCheckboxBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemDiseaseCheckboxBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemDiseaseCheckboxBinding> holder, int position, DiseaseItem currentItem) {
        ItemDiseaseCheckboxBinding binding = holder.binding;
        binding.setDisease(currentItem);

        boolean isSelected = Objects.requireNonNull(viewModel.getSelectedDiseases().getValue()).contains(currentItem);
        binding.checkboxDisease.setChecked(isSelected);

        if (isDialogAdapter) {
            // ✅ Allow clicking in dialog
            binding.checkboxDisease.setOnCheckedChangeListener((buttonView, isChecked) -> {
                viewModel.toggleDiseaseSelection(currentItem);
            });
        } else {
            // ❌ Disable clicking in fragment
            binding.checkboxDisease.setEnabled(false);
        }

        binding.executePendingBindings();
    }

    // 🔎 Filter method for searching diseases
    public void filter(String query) {
        List<DiseaseItem> filteredList = new ArrayList<>();
        for (DiseaseItem disease : fullList) {
            if (disease.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(disease);
            }
        }
        updateList(filteredList);
    }

    // 🔎 Update list method
    void updateList(List<DiseaseItem> newList) {
        items = newList;
        notifyDataSetChanged();
    }
}
