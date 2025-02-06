package com.project.homeplantcare.ui.admin_screen.add_plant_admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.homeplantcare.databinding.ItemDiseaseCheckboxBinding;
import com.project.homeplantcare.models.DiseaseItem;
import com.project.homeplantcare.ui.base.BaseAdapter;

import java.util.List;
import java.util.Objects;

public class DiseasesSelectionAdapter extends BaseAdapter<DiseaseItem, ItemDiseaseCheckboxBinding> {

    private final AddPlantViewModel viewModel;

    public DiseasesSelectionAdapter(List<DiseaseItem> itemList, AddPlantViewModel viewModel) {
        super(itemList);
        this.viewModel = viewModel;
    }

    @Override
    public ItemDiseaseCheckboxBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemDiseaseCheckboxBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemDiseaseCheckboxBinding> holder, int position, DiseaseItem currentItem) {
        ItemDiseaseCheckboxBinding binding = holder.binding;
        binding.setDisease(currentItem);

        // Observe and update checkbox selection state
        binding.checkboxDisease.setChecked(viewModel.getSelectedDiseases().getValue().contains(currentItem));

        binding.checkboxDisease.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.toggleDiseaseSelection(currentItem);
        });

        binding.executePendingBindings();
    }
}

